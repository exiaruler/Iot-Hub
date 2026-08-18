import { createSlice } from "@reduxjs/toolkit";
interface LoginState{
    username:string;
    timeout:number;
    name:string;
    id:string;
    role:string;
    login:boolean;
    expiry:Date|null;
}
export const loginSlice=createSlice({
    name: "login",
    initialState:<LoginState>{
        username:"",
        timeout:0,
        name:"",
        id:"",
        role:"",
        login:false,
        expiry:null
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
            if(action.payload.expiry!=null){
                state.expiry=new Date(action.payload.expiry);
            }
        },
        clearUser:(state)=>{
            state.id="";
            state.name="";
            state.login=false;
            state.role="";
            state.timeout=0;
            state.expiry=null;
        },
        setTimeout:(state,action)=>{
            state.timeout=action.payload;
             if(action.payload.expiry!=null){
                state.expiry=new Date(action.payload.expiry);
            }
        },
        
    }
});
export const{setTimeout,setUsername,setUser,clearUser}=loginSlice.actions;
export const getUser = (state:any) => state.login;
export const getLoginState=(state:Record<string,any>)=>state.login.login;
export default loginSlice.reducer;