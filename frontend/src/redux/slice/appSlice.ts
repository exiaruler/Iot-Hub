import { createSlice } from "@reduxjs/toolkit";
import Util from "../../base/Util";
const util=new Util();

export const appSlice=createSlice({
    name:"app",
    initialState:{
        id:"",
        application:"",
        pages:[],
        contents:[],
    },
    reducers:{
        // set all contents
        setApp:(state,action)=>{
            state.contents=action.payload.contents;
            state.id=action.payload._id;
            state.pages=action.payload.pages;
            state.application=action.payload.application;
            util.sessionSet("app",action.payload);
        },
        // set individual content
        setContent:(state,action)=>{
            var content=action.payload;
            var arr:any=state.contents;
            var exist=arr.find((con:Record<string,any>)=>con.pathname===content.pathname);
            if(exist!=null){
                var exIndex=arr.indexOf(exist);
                arr[exIndex]=content;
            }else arr.push(content);
            state.contents=arr;
            util.sessionSet("app",state);
        }   
    }
});
export const{setApp}=appSlice.actions;
export const{setContent}=appSlice.actions;
export const getApp=(state:Record<string,any>)=>state.app;
export const getAppId=(state:Record<string,any>)=>state.app.id;

export const getPages=(state:Record<string,any>)=>state.app.pages;
export const getContents=(state:Record<string,any>)=>state.app.contents;
export default appSlice.reducer;
