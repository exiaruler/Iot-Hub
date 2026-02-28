import { createSlice } from "@reduxjs/toolkit";
export const loginSlice=createSlice({
    name: "login",
    initialState:{
        username:"",
        timeout:0,
        name:"",
        id:"",
        role:"",
        login:false
    },
    reducers: {
        setUsername:(state,action)=>{
            state.username=action.payload;
        },
        setUser:(state,action)=>{
            state.id=action.payload.id;
            state.name=action.payload.name;
            state.login=true;
            state.role=action.payload.role;
            state.timeout=action.payload.timeout;
            
        },
        clearUser:(state)=>{
            state.id="";
            state.name="";
            state.login=false;
            state.role="";
            state.timeout=0;
        },
        setTimeout:(state,action)=>{
            state.timeout=action.payload;
        },
        
    }
});
export const{setTimeout,setUsername,setUser,clearUser}=loginSlice.actions;

export const getUser = (state:any) => state.login;
export const getLoginState=(state:Record<string,any>)=>state.login.login;
export default loginSlice.reducer;