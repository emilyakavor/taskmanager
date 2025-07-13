import type {Task} from "./components/TaskBoard.tsx";

const BASE_URL = '/api/tasks';

const getAuthHeaders = () => {
    const token = localStorage.getItem('token');
    return {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
    };
};

export const fetchTasks = async () => {
    const res = await fetch(BASE_URL, {headers: getAuthHeaders()});
    return res.json();
};

export const createTask = async (task: Task) => {
    const res = await fetch(BASE_URL, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(task),
    });
    return res.json();
};

export const updateTask = async (id: number, task: Task) => {
    const res = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify(task),
    });
    return res.json();
};

export const deleteTask = async (id: number) => {
    return fetch(`${BASE_URL}/${id}`, { method: 'DELETE', headers: getAuthHeaders() });
};

export const login = async (email: string, password: string) => {
    const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({ email, password }),
    });

    if (!response.ok) throw new Error('Login failed');
    return response.json(); // { token: string }
};

