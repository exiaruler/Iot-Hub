'use client'
import { getLoginState } from "@/redux/slice/loginSlice";
import { ReactNode } from "react"
import { useSelector } from "react-redux";

interface Props{
    children?:ReactNode;
}
export default function Login(props:Props){
    const login=useSelector(getLoginState);
    return(
        <>
        {login?
            props.children
        :null}
        </>
    )
}