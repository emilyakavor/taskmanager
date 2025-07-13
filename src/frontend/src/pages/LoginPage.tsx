import React, { useState } from 'react';
import { login } from "../api.ts";

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        try {
            const { token } = await login(email, password);
            localStorage.setItem('token', token);
            window.location.href = '/dashboard';
        } catch (err) {
            setError('Login failed. Please check your credentials and try again.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-500 via-indigo-600 to-purple-600 px-4">
            <div className="max-w-md w-full bg-white rounded-3xl shadow-xl p-8">
                <h2 className="text-3xl font-extrabold text-center text-gray-800 mb-6">Welcome Back</h2>
                <form onSubmit={handleLogin} className="space-y-6">
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email Address</label>
                        <input
                            id="email"
                            type="email"
                            autoComplete="email"
                            required
                            className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-xl shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="you@example.com"
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                        <input
                            id="password"
                            type="password"
                            autoComplete="current-password"
                            required
                            className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-xl shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="••••••••"
                        />
                    </div>
                    <div className="flex justify-between items-center text-sm">
                        <label className="flex items-center">
                            <input type="checkbox" className="mr-2" />
                            Remember me
                        </label>
                        <a href="#" className="text-indigo-600 hover:underline">Forgot password?</a>
                    </div>
                    <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-xl shadow-md hover:bg-indigo-700 transition-colors text-lg font-semibold">
                        Sign In
                    </button>
                    {error && (
                        <div className="text-red-600 bg-red-50 mt-4 px-4 py-2 rounded-lg border border-red-300 text-sm text-center">
                            {error}
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
