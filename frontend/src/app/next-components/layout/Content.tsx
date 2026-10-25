'use client'
import { NextBase,ObjectArray, ObjectRecord } from "@/NextBase";
import { NextUIBase } from "@/NextUIBase";
import { getLoginState } from "@/redux/slice/loginSlice";
import { usePathname, useRouter } from 'next/navigation';
import { ReactNode, RefObject, Suspense, forwardRef, useEffect, useImperativeHandle, useRef, useState } from "react";
import { Alert, Container } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
export type {ObjectRecord} from "@/NextBase";
export type {ObjectArray} from "@/NextBase";
interface Props {
    children?: ReactNode;
}

export interface ContentRef {
    generateKeyClient: () => void;
    checkKey: () => string | null;
    addInputRefComponent: (array: RefObject<Array<any>>, component: any) => RefObject<Array<any>>;
    forceUpdateRefComponents: (array: RefObject<Array<any>>) => void;
    getQuery:()=>URLSearchParams;
    getQueryField:(field:string)=>string|null;
    convertMiliSecondsToTime:(mills:number)=>{ hours: number; minutes: number; seconds: number };
    convertMillisecondsToTimeString:(mills:number)=>string;
    updateArrayByIndex:(data:ObjectRecord,index:number,array:ObjectArray)=>ObjectArray;
    pushToArray:(data:ObjectRecord,array:ObjectArray)=>ObjectArray;
    filteredArrayByDateTime:(array:ObjectArray,key:string,condition?:boolean)=>ObjectArray;
    showAlert:(message:string,variant?:string,duration?:number)=>void;
    passDateTime:(dateTime:Date)=>boolean;
    fetchClient:(url:string,method:string,body?:Record<string,any>|null)=>Promise<Response|void>;
    login: boolean;
    user: Record<string, any>;
    location: string|null;
    util: NextBase;
    pages: ObjectArray;
    router: ReturnType<typeof useRouter>;
}

const Content = forwardRef<ContentRef, Props>((props, ref) => {
    const login = useSelector((state: ObjectRecord) => state!.login.login);
    const user = useSelector((state:ObjectRecord) => state!.login);
    const pages = useSelector((state:ObjectRecord) => state!.page.pages);
    const expiry= useSelector((state: ObjectRecord) => state!.login.expiry);
    const util = new NextBase();
    const router=useRouter();
    const location = usePathname();
    const [alert,setAlert]=useState({
        'show':false,
        'message':'',
        'variant':'primary'
    });

    useImperativeHandle(ref, () => ({
        generateKeyClient: () => {
            const key = util.generateEncryptKey();
            sessionStorage.setItem(util.originUrl + "-en", key);
        },
        updateArrayByIndex(data:ObjectRecord,index:number,array:ObjectArray): ObjectArray {
            return [...array.slice(0, index), data, ...array.slice(index + 1)] as ObjectArray;
        },
        pushToArray(data:ObjectRecord,array:ObjectArray){
            return [...array,data] as ObjectArray;
        },
        checkKey: () => {
            return sessionStorage.getItem(util.originUrl + "-en");
        },
        addInputRefComponent: (array: RefObject<Array<any>>, component: any) => {
            const exists = array.current.find((ele: any) => component === ele) || null;
            if (exists === null) array.current.push(component);
            return array;
        },
        fetchClient:async (url:string,method:string,body:Record<string,any>|null=null):Promise<Response|void>=>{
            try{
                const request=await util.fetch(url,method,body);
                if (!request.ok) {
                    const error = new Error(`HTTP Error: ${request.status}`);
                    throw error;                    // Force execution into the catch block
                }
                if(request.ok){
                    const data=await request.json();
                    const status=await request.status;
                    if(data!=null){
                        request.json=data;
                    }
                }
                return request;
            }catch(error:unknown){
                if(error instanceof Error){
                    showAlert(error.message, "danger", 5000);
                }
            }
        },
        forceUpdateRefComponents: (array: RefObject<Array<any>>) => {
            array.current.forEach((ele: any) => {
                ele?.forceUpdate?.();
            });
        },
        getQuery:()=>{
            return new URLSearchParams(window.location.search);
        },
        getQueryField:(field:string)=>{
            const query= new URLSearchParams(window.location.search);
            return query.get(field);
        },
        passDateTime:(dateTime:Date):boolean=>{
            return passDateTime(dateTime);
        },
        filteredArrayByDateTime:(array:ObjectArray,key:string,condition:Boolean=false):ObjectArray=>{
            const filteredQueue = array.filter((q:ObjectRecord)=>{
            return q?.[key] == null || condition==passDateTime(q?.[key]);
            });
            return filteredQueue;
        },
        convertObjectArrayToArray:(obj:ObjectArray):Record<string, any>[]=>{
            if(obj==null) return [];
            const arr:Record<string, any>[]=[];
            obj.forEach((ele:ObjectRecord)=>{
                if(ele!=null) arr.push(ele);
            });
            return arr;
        },
        convertMiliSecondsToTime:(mills:number):{ hours: number; minutes: number; seconds: number }=>{
            const totalSeconds = Math.floor(mills / 1000);
            const seconds = totalSeconds % 60;
            const totalMinutes = Math.floor(totalSeconds / 60);
            const minutes = totalMinutes % 60;
            const hours = Math.floor(totalMinutes / 60);
            return { hours, minutes, seconds };
        },
        convertMillisecondsToTimeString:(mills:number):string=>{
            const { hours, minutes, seconds } = convertMiliSecondsToTime(mills);
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        },
        showAlert:(message:string,variant:string='primary',duration:number=5000):void=>{
            showAlert(message,variant,duration);
        },
        login,
        user,
        location,
        util,
        pages,
        router
    }));
    const expiryCheck=()=>{
        
    }
    const showAlert=(message:string,variant:string='primary',duration:number=5000):void=>{
        setAlert({ show: true, message, variant });
        if (duration > 0) {
            setTimeout(() => {
                setAlert({ show: false, message: '', variant });
            }, duration);
        }
    }

    /*
    useEffect(()=>{
        const timer=window.setInterval(expiryCheck,5000);
        return ()=>window.clearInterval(timer);
    },[])
    */
    return (
            <Container>
            <div style={{ position: "relative" }}>
                {props.children}
                {alert.show ? (
                    <div style={{ position: "absolute", inset: 0, zIndex: 10, display: "flex", justifyContent: "center", alignItems: "flex-start", pointerEvents: "none" }}>
                        <Alert variant={alert.variant} style={{ pointerEvents: "auto" }}>
                            {alert.message}
                        </Alert>
                    </div>
                ) : null}
            </div>
            </Container>
    );
});
export default Content;
function passDateTime(dateTime: Date | null): boolean {
    if (dateTime !== null) {
        const dt = new Date();
        const compareDt = new Date(dateTime);
        if (dt > compareDt) {
            return true;
        }
    }
    return false;
}
function convertMiliSecondsToTime(mills: number): { hours: any; minutes: any; seconds: any; } {
    const totalSeconds = Math.floor(mills / 1000);
    const seconds = totalSeconds % 60;
    const totalMinutes = Math.floor(totalSeconds / 60);
    const minutes = totalMinutes % 60;
    const hours = Math.floor(totalMinutes / 60);
    return { hours, minutes, seconds };
}

