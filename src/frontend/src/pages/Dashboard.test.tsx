import { render, screen } from '@testing-library/react';
import Dashboard from './Dashboard.tsx';

test('renders dashboard and sample task', () => {
  render(<Dashboard />);
  expect(screen.getByText(/Sample Task/i)).toBeInTheDocument();
});