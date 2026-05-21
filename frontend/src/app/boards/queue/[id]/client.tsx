'use client'

import BackButton from "@/app/next-components/buttons/BackButton";
import Content, { ContentRef, ObjectArray } from "@/app/next-components/layout/Content"
import TableComponent from "@/app/next-components/TableComponent"
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { use, useRef } from "react";
import { Col, Row } from "react-bootstrap"

interface Props{
    queue:Record<string, any>[];
    boardId:string;
}
export default function Client(props:Props){
    const contentRef=useRef<ContentRef>(null);
    const convertTime=(dateTime:string)=>{
        return new Date(dateTime).toLocaleTimeString();
    }
    const convertDate=(dateTime:string)=>{
        return new Date(dateTime).toLocaleDateString();
    }
    const convertMiliSecondsToTime=(mills:number):{ hours: number; minutes: number; seconds: number }=>{
        const totalSeconds = Math.floor(mills / 1000);
        const seconds = totalSeconds % 60;
        const totalMinutes = Math.floor(totalSeconds / 60);
        const minutes = totalMinutes % 60;
        const hours = Math.floor(totalMinutes / 60);
        return { hours, minutes, seconds };
    }
    const convertMillToDisplayTime=(rawTime:number)=>{
        const time=convertMiliSecondsToTime(rawTime);
        let output="";
        if(time?.hours>0){
            output+=`${time.hours}h `;
        }
        if(time?.minutes>0){
            output+=`${time.minutes}m `;
        }
        if(time?.seconds>=0){
            output+=`${time.seconds}s`;
        }
        return output || "0s";
    }
    return(
        <Content ref={contentRef}>
        <Row>
        <Col md={2} xs={2}>
        <BackButton url={'/boards/'+props.boardId} />
        </Col>
        <Col md={10} xs={14}>
        <TableComponent results={props.queue} idKey={"id"}>
        <TableComponentColumn key={"boardTaskId"} columnName={"Task ID"}/>
        <TableComponentColumn key={"taskName"} columnName={"Task"}/>
        <TableComponentColumn key={"inBoardQueue"} columnName={"In Queue"}/>
        <TableComponentColumn key={"systemQueue"} columnName={"System Task"}/>
        <TableComponentColumn key={"delay"} columnName={"Occurrence"} functionDisplay={convertMillToDisplayTime}/>
        <TableComponentColumn key={"createdDate"} columnName={"Created Date"} functionDisplay={convertDate}/>
        <TableComponentColumn key={"createdDate"} columnName={"Created Time"} functionDisplay={convertTime}/>
        </TableComponent>
        </Col>
        </Row>
        </Content>
    )
}