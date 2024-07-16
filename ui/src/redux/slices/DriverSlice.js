import { createSlice } from "@reduxjs/toolkit";


const driverSlice = createSlice(
    {
        name: 'drivers',
        initialState: {
            drivers: []
        },
        reducers: {
            addDriver: (state,action) => {
                const driver = action.payload;
                if ( driver !== undefined) {
                    state.drivers = [...state.drivers,driver]
                }
            },
            removeDriver: (state,action) => {
                const id = action.payload;
                state.drivers =  state.drivers.filter(driver => driver.id !== id);
            },
            addDriverList: (state,action) => {
                state.drivers = action.payload;
            }
        }
    }
)


export const {

    addDriver,
    addDriverList,
    removeDriver

} = driverSlice.actions;

export default driverSlice.reducer;