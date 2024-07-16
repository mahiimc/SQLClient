import { Outlet, RouterProvider } from 'react-router-dom';
import './App.css';
import AppLayout from './components/layout/AppLayout';
import { router } from './router/Router';


function App() {


  return (
    <div className="App">
      <RouterProvider router={router}>
        <AppLayout/>
        </RouterProvider>
    </div>
  );
}

export default App;
