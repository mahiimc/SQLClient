import { configureStore } from '@reduxjs/toolkit';
import DriverSlice from './slices/DriverSlice';


const store = configureStore({
    reducer: {
        driver: DriverSlice
    }
})

export default store;