import React, { useEffect, useState } from 'react';
import { createTask, deleteTask, fetchTasks, updateTask } from "../api";
import {
    DragDropContext,
    Droppable,
    Draggable,
} from 'react-beautiful-dnd';
import type {DropResult} from 'react-beautiful-dnd';


type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

export interface Task {
    id?: number;
    title: string;
    description: string;
    status: TaskStatus;
}

const TaskBoard: React.FC = () => {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [newTaskStatus, setNewTaskStatus] = useState<TaskStatus | null>(null);
    const [newTitle, setNewTitle] = useState('');
    const [newDescription, setNewDescription] = useState('');
    const [editingTask, setEditingTask] = useState<Task | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const loadTasks = async () => {
            setIsLoading(true);
            try {
                const fetchedTasks = await fetchTasks();
                setTasks(fetchedTasks);
            } catch (error) {
                console.error('Failed to load tasks:', error);
            } finally {
                setIsLoading(false);
            }
        };
        loadTasks().then(() => console.log("Loading done"));
    }, []);

    if (isLoading) {
        return <div className="flex justify-center items-center h-screen">Loading...</div>;
    }

    const handleCreate = async () => {
        if (!newTitle.trim() || !newTaskStatus) return;

        try {
            const newTask = {
                title: newTitle,
                description: newDescription,
                status: newTaskStatus,
            };
            const saved = await createTask(newTask);
            setTasks(prev => [...prev, saved]);
            setNewTitle('');
            setNewDescription('');
            setNewTaskStatus(null);
        } catch (error) {
            console.error('Failed to create task:', error);
            // Add user notification here
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await deleteTask(id);
            setTasks(prev => prev.filter(t => t.id !== id));
        } catch (error) {
            console.error('Failed to delete task:', error);
            // Add user notification here
        }
    };

    const handleEditSave = async () => {
        if (!editingTask || !editingTask.id) return;

        try {
            const updated = await updateTask(editingTask.id, editingTask);
            setTasks(prev => prev.map(t => (t.id === updated.id ? updated : t)));
            setEditingTask(null);
        } catch (error) {
            console.error('Failed to update task:', error);
            // Add user notification here
        }
    };

    const groupedTasks = (status: TaskStatus) =>
        tasks.filter(task => task.status === status);


    const onDragEnd = async (result: DropResult) => {
        const { source, destination, draggableId } = result;

        // Return if dropped outside or in the same position
        if (!destination) return;
        if (
            source.droppableId === destination.droppableId &&
            source.index === destination.index
        ) return;

        const draggedTask = tasks.find(t => t.id?.toString() === draggableId);
        if (!draggedTask) return;

        // Create new array of tasks
        const newTasks = Array.from(tasks);
        // Remove task from source
        const [removed] = newTasks.splice(
            newTasks.findIndex(t => t.id === draggedTask.id),
            1
        );

        // Update task status
        const updatedTask = {
            ...removed,
            status: destination.droppableId as TaskStatus
        };

        // Insert at new position
        newTasks.splice(
            destination.index,
            0,
            updatedTask
        );

        // Optimistically update UI
        setTasks(newTasks);

        try {
            // Update in backend
            await updateTask(updatedTask.id!, updatedTask);
        } catch (error) {
            console.error('Failed to update task:', error);
            // Revert to original state if update fails
            setTasks(tasks);
        }
    };

    return (
        <DragDropContext onDragEnd={onDragEnd}>
            <div className="flex justify-between p-6 gap-4">
                {(['TODO', 'IN_PROGRESS', 'DONE'] as TaskStatus[]).map(status => (
                    <Droppable droppableId={status} key={status}>
                        {(provided, snapshot) => (
                            <div
                                ref={provided.innerRef}
                                {...provided.droppableProps}
                                className={`bg-gray-100 rounded-lg p-4 w-1/3 shadow-md min-h-[400px] ${
                                    snapshot.isDraggingOver ? 'bg-gray-200' : ''
                                }`}
                            >
                                <h2 className="text-xl font-bold text-gray-700 mb-3">{status.replace('_', ' ')}</h2>

                                {groupedTasks(status).map((task, index) => (
                                    <Draggable draggableId={task.id!.toString()} index={index} key={task.id}>
                                        {(provided, snapshot) => (
                                            <div
                                                ref={provided.innerRef}
                                                {...provided.draggableProps}
                                                {...provided.dragHandleProps}
                                                className={`bg-white rounded p-3 shadow mb-3 relative ${
                                                    snapshot.isDragging ? 'shadow-lg' : ''
                                                }`}
                                                style={{
                                                    ...provided.draggableProps.style
                                                }}
                                            >
                                                {editingTask?.id === task.id ? (
                                                    <>
                                                        <input
                                                            value={editingTask!.title}
                                                            onChange={e => setEditingTask({ ...editingTask!, title: e.target.value })}
                                                            className="border w-full rounded px-2 py-1 mb-1"
                                                        />
                                                        <textarea
                                                            value={editingTask!.description}
                                                            onChange={e => setEditingTask({ ...editingTask!, description: e.target.value })}
                                                            className="border w-full rounded px-2 py-1 mb-1"
                                                        />
                                                        <div className="flex justify-end gap-2">
                                                            <button
                                                                className="text-sm px-2 py-1 bg-green-500 text-white rounded"
                                                                onClick={handleEditSave}
                                                            >
                                                                Save
                                                            </button>
                                                            <button
                                                                className="text-sm px-2 py-1 bg-gray-400 text-white rounded"
                                                                onClick={() => setEditingTask(null)}
                                                            >
                                                                Cancel
                                                            </button>
                                                        </div>
                                                    </>
                                                ) : (
                                                    <>
                                                        <h4 className="font-semibold">{task.title}</h4>
                                                        <p className="text-sm text-gray-600">{task.description}</p>
                                                        <div className="absolute top-1 right-2 flex gap-2 text-xs">
                                                            <button
                                                                className="text-blue-600 hover:underline"
                                                                onClick={() => setEditingTask(task)}
                                                            >
                                                                Edit
                                                            </button>
                                                            <button
                                                                className="text-red-500 hover:underline"
                                                                onClick={() => handleDelete(task.id!)}
                                                            >
                                                                Delete
                                                            </button>
                                                        </div>
                                                    </>
                                                )}
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}

                                {/* Task form */}
                                {newTaskStatus === status ? (
                                    <div className="mt-4">
                                        <input
                                            className="w-full border rounded p-2 mb-2"
                                            placeholder="Title"
                                            value={newTitle}
                                            onChange={e => setNewTitle(e.target.value)}
                                        />
                                        <textarea
                                            className="w-full border rounded p-2 mb-2"
                                            placeholder="Description"
                                            value={newDescription}
                                            onChange={e => setNewDescription(e.target.value)}
                                        />
                                        <div className="flex justify-end gap-2">
                                            <button
                                                onClick={handleCreate}
                                                className="bg-blue-500 text-white px-3 py-1 rounded text-sm"
                                            >
                                                Add
                                            </button>
                                            <button
                                                onClick={() => setNewTaskStatus(null)}
                                                className="bg-gray-400 text-white px-3 py-1 rounded text-sm"
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </div>
                                ) : (
                                    <button
                                        onClick={() => setNewTaskStatus(status)}
                                        className="mt-2 w-full text-sm text-white bg-green-500 hover:bg-green-600 rounded p-2"
                                    >
                                        + Add Task
                                    </button>
                                )}
                            </div>
                        )}
                    </Droppable>
                ))}
            </div>
        </DragDropContext>
    );
};

export default TaskBoard;