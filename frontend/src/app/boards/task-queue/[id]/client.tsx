'use client'
import Content, { ContentRef, ObjectArray } from "@/app/next-components/layout/Content"
import TableComponent from "@/app/next-components/TableComponent"
import BackButton from "@/components/Buttons/BackButton"
import TableComponentColumn from "@/components/Table/TableComponentColumn"
import { useEffect, useRef, useState } from "react"
import { Row, Col } from "react-bootstrap"
interface Props{
    queue:ObjectArray;
    boardId:string;
}
export default function Client(props:Props){
    const contentRef=useRef<ContentRef>(null);
    const [queue,setQueue]=useState<ObjectArray>(props.queue);

    const showData=(date:Date)=>{
        return new Date(date).toDateString();
    }
    const showTime=(date:Date)=>{
        return new Date(date).toLocaleTimeString();
    }
    const updateQueue=()=>{
        const content=contentRef.current;
        if(content){
            const filteredQueue=content.filteredArrayByDateTime(queue,"scheduledTime");
            setQueue(filteredQueue);
        }
    }
    useEffect(()=>{
        setInterval(updateQueue,1000);
    },[])
    return(
        <Content ref={contentRef}>
        <Row>
        <Col md={2} xs={2}>
        
        </Col>
        <Col md={10} xs={14}>
        <TableComponent results={queue} idKey={"id"}>
        <TableComponentColumn key={"application"} columnName={"Task"}/>
        <TableComponentColumn key={"scheduledTime"} columnName={"Scheduled Time"} functionDisplay={showTime}/>
        <TableComponentColumn key={"scheduledTime"} columnName={"Scheduled Date"} functionDisplay={showData}/>       
        </TableComponent>
        </Col>
        </Row>
        </Content>
    )
}