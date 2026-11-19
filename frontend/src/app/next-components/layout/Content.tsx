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
    stringTime:(dateTime:string|Date|null)=>string;
    stringDate:(dateTime:string|Date|null)=>string;
    convertMiliSecondsToTime:(mills:number)=>{ hours: number; minutes: number; seconds: number };
    convertMillisecondsToTimeString:(mills:number)=>string;
    updateArrayByIndex:(data:ObjectRecord,index:number,array:ObjectArray)=>ObjectArray;
    pushToArray:(data:ObjectRecord,array:ObjectArray)=>ObjectArray;
    filteredArrayByDateTime:(array:ObjectArray,key:string,condition?:boolean)=>ObjectArray;
    convertObjectArrayToArray:(obj:ObjectArray|null)=>Record<string, any>[];
    showAlert:(message:string,variant?:string,duration?:number)=>void;
    passDateTime:(dateTime:string|Date|null)=>boolean;
    regularUpdateOccurrence:(name:string,nextUpdateDate:Date|null,enabled:boolean,update:()=>Promise<void>,duration?:number)=>Promise<void>;
    fetchClient:(url:string,method:string,body?:Record<string,any>|null)=>Promise<Response|void>;
    login: boolean;
    user: Record<string, any>;
    location: string|null;
    util: NextBase;
    pages: ObjectArray;
    router: ReturnType<typeof useRouter>;
}
interface RoutineCheck{
    name:string;
    nextUpdateDate:Date|null;
    lastNextOp:Date|null;
    nextOpPassCount:number|0;
    updateInProgress:boolean;
    updateTime:number|1000;
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
    const routines=useRef<RoutineCheck[]>([]);

    useImperativeHandle(ref, () => ({
        generateKeyClient: () => {
            generateKeyClient(util);
        },
        updateArrayByIndex(data:ObjectRecord,index:number,array:ObjectArray): ObjectArray {
            return updateArrayByIndex(data,index,array);
        },
        pushToArray(data:ObjectRecord,array:ObjectArray){
            return pushToArray(data,array);
        },
        checkKey: () => {
            return checkKey(util);
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
            return getQuery();
        },
        getQueryField:(field:string)=>{
            return getQueryField(field);
        },
        stringTime:(dateTime:string|Date|null):string=>{
            return stringTime(dateTime);
        },
        stringDate:(dateTime:string|Date|null):string=>{
            return stringDate(dateTime);
        },
        passDateTime:(dateTime:string|Date|null):boolean=>{
            return passDateTime(dateTime);
        },
        regularUpdateOccurrence:async(name:string,nextUpdateDate:Date|null,enabled:boolean,update:()=>Promise<void>,durration?:number):Promise<void>=>{
            const date=nextUpdateDate==null?null:new Date(nextUpdateDate);
            let routine=getRoutineCheck(name);
            if(routine==null){
                routine=createRoutineCheck(name,date,durration);
            }else if(routine.nextUpdateDate?.getTime()!==date?.getTime()){
                routine.nextUpdateDate=date;
                routine.lastNextOp=date;
                routine.nextOpPassCount=0;
                routine.updateTime=1000;
            }

            if(date==null||!enabled||!passDateTime(date)) return;
            if(routine.lastNextOp?.getTime()!==date.getTime()){
                routine.lastNextOp=date;
                routine.nextOpPassCount=0;
                routine.updateTime=1000;
            }else{
                routine.updateTime+=1000;
            }
            if(routine.nextOpPassCount>=10||routine.updateInProgress) return;

            routine.nextOpPassCount++;
            routine.updateInProgress=true;
            try{
                await update();
            }finally{
                routine.updateInProgress=false;
            }
            updateRoutineCheck(routine);
        },
        filteredArrayByDateTime:(array:ObjectArray,key:string,condition:boolean=false):ObjectArray=>{
            return filteredArrayByDateTime(array,key,condition);
        },
        convertObjectArrayToArray,
        convertMiliSecondsToTime,
        convertMillisecondsToTimeString,
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
    // create new routine check
    const createRoutineCheck=(name:string,nextUpdateDate:Date|null,updateTime:number=1000):RoutineCheck=>{
        const routine:RoutineCheck={
            name,
            nextUpdateDate:nextUpdateDate,
            lastNextOp:null,
            nextOpPassCount:0,
            updateInProgress:false,
            updateTime
        };
        if(getRoutineCheck(name)!=null) throw new Error(`Routine with name "${name}" already exists.`);
        routines.current.push(routine);
        return routine;
    }
    // get routine check by name
    const getRoutineCheck=(name:string):RoutineCheck|null=>{
        const routine=routines.current.find((r:RoutineCheck)=>r.name===name);
        return routine||null;
    }
    const updateRoutineCheck=(routine:RoutineCheck):void=>{
        const index=routines.current.findIndex((r:RoutineCheck)=>r.name===routine.name);
        if(index!==-1){
            routines.current[index]=routine;
        }
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
export function generateKeyClient(util: NextBase = new NextBase()): void {
    const key = util.generateEncryptKey();
    sessionStorage.setItem(util.originUrl + "-en", key);
}
export function checkKey(util: NextBase = new NextBase()): string | null {
    return sessionStorage.getItem(util.originUrl + "-en");
}
export function getQuery(): URLSearchParams {
    return new URLSearchParams(window.location.search);
}
export function getQueryField(field: string): string | null {
    return getQuery().get(field);
}
export function stringTime(dateTime: string | Date | null): string {
    if (dateTime == null) return "";
    const date = new Date(dateTime);
    if (Number.isNaN(date.getTime())) return "";
    return date.toLocaleTimeString();
}
export function stringDate(dateTime: string | Date | null): string {
    if (dateTime == null) return "";
    const date = new Date(dateTime);
    if (Number.isNaN(date.getTime())) return "";
    return date.toLocaleDateString();
}
export function updateArrayByIndex(data: ObjectRecord, index: number, array: ObjectArray): ObjectArray {
    return [...array.slice(0, index), data, ...array.slice(index + 1)] as ObjectArray;
}
export function pushToArray(data: ObjectRecord, array: ObjectArray): ObjectArray {
    return [...array, data] as ObjectArray;
}
export function convertObjectArrayToArray(obj: ObjectArray | null): Record<string, any>[] {
    if (obj == null) return [];
    return obj.filter((entry): entry is Record<string, any> => entry != null);
}
export function filteredArrayByDateTime(array: ObjectArray, key: string, condition: boolean = false): ObjectArray {
    return array.filter((record: ObjectRecord) => {
        return record?.[key] == null || condition === passDateTime(record[key]);
    });
}
export function passDateTime(dateTime: string | Date | null): boolean {
    if (dateTime !== null) {
        const dt = new Date();
        const compareDt = new Date(dateTime);
        return dt > compareDt;
    }
    return false;
}
export function convertMiliSecondsToTime(mills: number): { hours: number; minutes: number; seconds: number } {
    const totalSeconds = Math.floor(mills / 1000);
    const seconds = totalSeconds % 60;
    const totalMinutes = Math.floor(totalSeconds / 60);
    const minutes = totalMinutes % 60;
    const hours = Math.floor(totalMinutes / 60);
    return { hours, minutes, seconds };
}
export function convertMillisecondsToTimeString(mills: number): string {
    const { hours, minutes, seconds } = convertMiliSecondsToTime(mills);
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}
