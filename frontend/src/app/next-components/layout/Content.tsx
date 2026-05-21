'use client'
import { NextBase,ObjectArray, ObjectRecord } from "@/NextBase";
import { NextUIBase } from "@/NextUIBase";
import { usePathname, useRouter } from 'next/navigation';
import { ReactNode, RefObject, Suspense, forwardRef, useImperativeHandle, useRef } from "react";
import { Container } from "react-bootstrap";
import { useSelector } from "react-redux";
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
    const util = new NextBase();
    //const nextBase = new NextBase();
    const router=useRouter();

    const location = usePathname();

    useImperativeHandle(ref, () => ({
        generateKeyClient: () => {
            const key = util.generateEncryptKey();
            sessionStorage.setItem(util.originUrl + "-en", key);
        },
        checkKey: () => {
            return sessionStorage.getItem(util.originUrl + "-en");
        },
        addInputRefComponent: (array: RefObject<Array<any>>, component: any) => {
            const exists = array.current.find((ele: any) => component === ele) || null;
            if (exists === null) array.current.push(component);
            return array;
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
        }
        ,
        convertMiliSecondsToTime:(mills:number):{ hours: number; minutes: number; seconds: number }=>{
            const totalSeconds = Math.floor(mills / 1000);
            const seconds = totalSeconds % 60;
            const totalMinutes = Math.floor(totalSeconds / 60);
            const minutes = totalMinutes % 60;
            const hours = Math.floor(totalMinutes / 60);
            return { hours, minutes, seconds };
        },
        login,
        user,
        location,
        util,
        pages,
        router
    }));

    return (
            <Container>
            {props.children}
            </Container>
    );
});
export default Content;

