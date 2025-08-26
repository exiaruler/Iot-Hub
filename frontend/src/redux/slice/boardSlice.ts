import { createSlice } from "@reduxjs/toolkit";
// individual board page current view
export const boardSlice=createSlice({
    name:"board",
    initialState:{
        currentBoard:0,
        deviceView:0,
        deviceTab:''
    },
    reducers:{
        setBoardView:(state,action)=>{
            state.currentBoard=action.payload;
        },
        setDeviceView:(state,action)=>{
            state.deviceView=action.payload;
        },
        setDeviceTabView:(state,action)=>{
            state.deviceTab=action.payload
        }
    }
});
