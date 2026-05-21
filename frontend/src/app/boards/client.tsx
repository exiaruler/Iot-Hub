'use client'
import GridCard from "@/components/GridCard";
import { Col, Row } from "react-bootstrap";
import ModalBox from "@/components/modal/ModalBox";
import { useEffect, useRef, useState } from "react";
import FormModal from "../next-components/modal/FormModal";
import TextInput from "../next-components/input/TextInput";
import Form from "../next-components/form/Form";
import SaveButton from "../next-components/buttons/SaveButton";
import NewButton from "../next-components/form/NewButton";
import SelectInput from "../next-components/input/SelectInput";
import Content, { ContentRef, ObjectArray, ObjectRecord } from "../next-components/layout/Content";
import Dev from "../next-components/user/dev";
interface Props{
    boards:ObjectArray;
    hardwares:ObjectArray;
    boardForm:ObjectRecord;
}
// user boards page
export default function Client(props:Props){
    const formRef=useRef<Form>(null);
    const modalRef=useRef<FormModal>(null);
    const gridRef:any=useRef(null);
    const contentRef=useRef<ContentRef>(null);
    const [boards,setBoards]=useState<ObjectArray>([]);
   
    const submitHandle=()=>{
        const form=formRef.current;
        if(form?.statusResponse==200){
            const data=form?.submissionResponse;
            setBoards([...boards,data]);
            form?.newRecord();
            modalRef.current?.close();
        }
        
    }
    const addHandle=(event:any)=>{
        let record=gridRef.current.getSelectedCard()||null;
        if(record==null){
            modalRef.current?.open();
        }else
        {
            contentRef.current?.router.push('/boards/'+record.boardId);
        }
    }
  
    const loadBoards=()=>{
        setBoards(props.boards);
    }
    
    useEffect(()=>{
        loadBoards();
    },[setBoards])
    return(
        <div>
        <Content ref={contentRef}>
        <Row>
        <Col md={2} xs={2}></Col>
        <Col md={10} xs={14}>
        <div>
        <GridCard ref={gridRef} mdCol={3} smCol={2} valueKey={"id"} data={boards} titleKey={"name"} bodyKey={""} onClick={addHandle} />
        </div>
        </Col>
        <Col>
        </Col>   
        </Row>
        <ModalBox ref={modalRef} title={"Add Board"} submitCaption="Add" hideSubmit={true}>
        <Form onSubmit={submitHandle} ref={formRef} recordLayout={{ id: 0, name: "", hardwardId:0,boardId:"" }} idKey={"id"} post={"/board/add-board-socket"}>
        <Row>
        <Col md={7} xs={9}>
        <TextInput formRef={formRef} label={"Name"} name="name" type={"text"} rows={0} required={true}/>
        <SelectInput formRef={formRef} label={"Board Model"} name={"hardwardId"} valueKey={"id"} required={true} displayKey={"boardName"} options={props.hardwares} type={""} rows={0}/>
        <Dev>
        <TextInput formRef={formRef} label={"Board Unique ID"} name={"boardId"} type={"text"} rows={0} required={false}/>
        </Dev>
        <SaveButton caption={"Save"} size={undefined}  />
        <NewButton formRef={formRef} caption={"Clear"} size={undefined}/>
        </Col>
        </Row>
        </Form>
        </ModalBox>
         </Content>
        </div>
    );
}