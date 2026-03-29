'use client'
import ModalBox from "@/components/modal/ModalBox"
import { RegularButton } from "@/app/next-components/buttons/RegularButton"
import TextInput from "@/app/next-components/input/TextInput"
import TabComponent from "@/components/Tab/TabComponent"
import TabGroup from "@/components/Tab/TabGroup"
import { Row, Col, Stack } from "react-bootstrap"
import { useEffect, useRef, useState } from "react"
import { NextUIBase } from "@/NextUIBase"
import TableComponent from "@/app/next-components/TableComponent"
import TableComponentColumn from "@/components/Table/TableComponentColumn"
import DeleteButton from "@/app/next-components/buttons/DeleteButton"
import ConfirmButton from "@/components/Buttons/ConfirmButton"
import AddForm from "./add-form"
import FormModal from "@/app/next-components/modal/FormModal"
import ModalButton from "@/components/Buttons/ModalButton"
import ConfigForm from "./config-form"
import { ContentRef, ObjectArray, ObjectRecord } from "@/app/next-components/layout/Content"
import Content from "@/app/next-components/layout/Content"
import Dev from "@/app/next-components/user/dev"
import DeleteModal from "@/app/next-components/modal/DeleteModal"
type JsonRecord ={
    key:string,
    value:any
}
interface Props{
    deviceForm:ObjectRecord;
    board:ObjectRecord;
    boardHardware:ObjectRecord;
}
// board page
export default function Client(props:Props){
    const contentRef=useRef<ContentRef>(null);
    const passwordModalRef=useRef<any>(null);
    const deleteModalRef:any=useRef(null);
    const updateModalRef=useRef<ModalButton>(null);
    const tabGrpRef=useRef<TabGroup>(null);
    const functionTblRef=useRef<TableComponent>(null);
    const formRefs:any=useRef([]);
    const [activated,setActivated]=useState(true);
    const [board,setBoard]=useState<ObjectRecord>(props.board||null);
    const [devices,setDevices]=useState<ObjectArray>(props.board?.device||[]);
    const [device,setDevice]=useState<ObjectRecord>(null);
    const [hardware,setHardware]=useState<ObjectRecord>(props.boardHardware||null);
    const uiBase=new NextUIBase();
    
    const openChangePass=()=>{
        passwordModalRef.current.open();
    }
    const openDelete=()=>{
        deleteModalRef.current.open();
    }
    
    const loadForms=(forms:ObjectRecord)=>{
        formRefs.current.push(forms);
    }
    const boardActive=()=>{
        if(board!=null&&!board.activated){
            setActivated(false);
        }
    }
    
    const handleTabSelect=(key:string):void=>{
        if(key!=="board"&&key!=="add"){
            let index=parseInt(key);
            setDevice(devices[index]);
        }
    }
    const status=(bool:boolean):string=>{
        let show="Inactive";
        if(bool) show="Active";
        return show;
    }
    const openFunction=()=>{
        let id=0;
        const funcRec=functionTblRef.current?.state.selectRowRec;
        if(funcRec!=null){
            id=funcRec.id;
        }
        contentRef.current!.router.push('/device/function/'+board?.boardId+'/'+device?.deviceId+'/'+id);
    }
    const deletehandle=()=>{
        contentRef.current!.router.push('/boards');
    }
    const deviceDeleteHandle=async (index:number)=>{
        const content=contentRef.current!;
        const request=await content.util.fetchClientQuery('/device/delete-device/'+devices[index]?.id,'DELETE');
        if(request.status==200){
            const arr=[...devices];
            arr.splice(index,1);
            setDevices(arr);
        }
    }
    const deleteFunctionHandle=async (deviceIndex:number)=>{
        const table=functionTblRef.current;
        const selectedRow=table?.returnRow();
        const content=contentRef.current!;
        if(selectedRow!=null){
            const id=selectedRow.id;
            const request=await content.util.fetchClientQuery('/route/delete-route/'+id,'DELETE');
            if(request.status==200){
                const arr=[...devices];
                const funcArr=arr[deviceIndex]?.routes;
                funcArr.splice(funcArr.findIndex((func:Record<string,any>)=>func.id==id),1);
                setDevices(arr);
            }
        }
    }
    const boardCommand=async(command:string)=>{
        const content=contentRef.current!;
        const request=await content.util.fetchClient('/task/'+command+'/'+board?.boardId,'POST',null);
        if(request.ok){
            let result=await request.json();
        }
    }
    const handleUpdate=(boardRec:Record<string,any>|null)=>{
       setBoard(boardRec);
    }
    const handleAddDevice=(record:ObjectRecord)=>{
        const devs=devices;
        const tab=tabGrpRef.current;
        devs.push(record);
        setDevices([...devs]);
    }
    const showDate=(dateTime:Date)=>{
        return new Date(dateTime).toDateString();
    }
    const showTime=(dateTime:Date)=>{
        const dt= new Date(dateTime);
        const time=dt.toLocaleTimeString();
        return time;
    } 
    useEffect(()=>{
        loadForms(props.deviceForm);
        boardActive();
    },[])
    return(
        <div>
        <Content ref={contentRef}>
        <Row>
        <Col md={2} xs={2}></Col>
        <Col md={10} xs={14}>
        <TabGroup defaultActiveKey={"board"} ref={tabGrpRef} onSelect={handleTabSelect}>
        <TabComponent title={board?.name||''} eventKey={"board"}>
        <div>
        <Row>
        <Col md={3} xs={9}>
        <TextInput label={"Board ID"} type={""} rows={0} value={board?.boardId} readOnly={true}/>
        <TextInput label={"Board Model"} type={""} rows={0} value={hardware?.boardName} readOnly={true}/>
        <TextInput label={"Status"} type={""} rows={0} value={status(board?.activated)} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <TextInput label={"RAM Usage"} type={""} rows={0} value={board?.ramUsage} readOnly={true}/>
        <TextInput label={"Total RAM"} type={""} rows={0} value={hardware?.maxRam} readOnly={true}/>
        <TextInput label={"Local IP"} type={""} rows={0} value={board?.ip} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <TextInput label={"Routine Check"} type={""} rows={0} value={board?.periodicCheck} readOnly={true}/>
        <TextInput label={"Last Connection Date"} type={""} rows={0} value={showDate(board?.lastConnectDateTime)} readOnly={true}/>
        <TextInput label={"Last Connection Time"} type={""} rows={0} value={showTime(board?.lastConnectDateTime)} readOnly={true}/>
        </Col>
        </Row>
        <Row>
        <Col>
        <Stack direction="horizontal" gap={2} className="mt-3">
        <RegularButton caption={"Change Password"} onClick={openChangePass} size={undefined} type={undefined}/>
        <ModalButton ref={updateModalRef} buttonCaption={"Configurations"} title={"Configured Board"} submitCaption={"Save"}>
        <ConfigForm submissionHandle={handleUpdate} record={board} modalRef={updateModalRef}/>
        </ModalButton>
        <ConfirmButton disabled={!activated} buttonCaption={"Update"} title={"Update Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('update')}>
        <p>Are you sure you want to update?</p>
        </ConfirmButton>
        <Dev>
        <ConfirmButton disabled={!activated} buttonCaption={"Upload"} title={"Upload Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('update')}>
        <p>Are you sure you want to upload?</p>
        <p>Board will no longer receive commands from the Proto until new firmware is uploaded from IDE or manual reset</p>
        </ConfirmButton>
        </Dev>
        <ConfirmButton disabled={!activated} buttonCaption={"Reset"} title={"Reset Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('restart')}>
        <p>Are you sure you want to restart board?</p>
        </ConfirmButton>
        <ConfirmButton disabled={!activated} buttonCaption={"Reset Board Configuration"} title={"Reset Board Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('restart-board-config')}>
        <p>Are you sure you want to reset board configurations?</p>
        <p>Board configurations will be wiped and board will be deactivated until reactivated</p>
        </ConfirmButton>
        <RegularButton caption={"Delete Board"} size={undefined} type={undefined} onClick={openDelete}/>
        </Stack>
       
        </Col>    
        </Row>
        </div>
        </TabComponent>
        {
            devices.map((dev:ObjectRecord,key:number)=>(
                <TabComponent title={dev?.name} eventKey={key} disabled={!activated}>
                                <TabGroup defaultActiveKey={"overview"}>
                                <TabComponent title={"Overview"} eventKey={"overview"}>
                                <Row>
                                <Col>
                                <RegularButton caption={"Update Device"} size={undefined} type={undefined}/>
                                <RegularButton onClick={()=>deviceDeleteHandle(key)} caption={"Delete Device"} size={undefined} type={undefined}/>
                                </Col>
                                </Row>
                                </TabComponent>
                                <TabComponent title={"Functions"} eventKey={"functions"}>
                                <Row>
                                <Col>
                                <TableComponent ref={functionTblRef} results={dev?.routes} idKey={"id"} rowSelect={true} onDoubleClick={openFunction}>
                                <TableComponentColumn key={"route"} columnName={"Function"} />
                                <TableComponentColumn columnName={"Startup"} key={""} size={5}/>
                                <TableComponentColumn key={"modes"} columnName={"Modes"} size={5}/>
                                </TableComponent>
                                </Col>
                                </Row>
                                <Row>
                                <Col>
                                <RegularButton caption={"Add Function"} size={undefined} type={undefined} onClick={openFunction}/>
                                <DeleteButton caption={"Delete Function"} onClick={()=>deleteFunctionHandle(key)} size={undefined} type={undefined}/>
                                </Col>
                                </Row>
                                </TabComponent>
                                </TabGroup>
                            </TabComponent>   
            ))
        }
        <TabComponent title={"Add Device"} eventKey={"add"} disabled={!activated}>
        <AddForm onUpdate={handleAddDevice} boardId={board?.id.toString()||""}/>
        </TabComponent>
        </TabGroup>
        </Col>
        </Row> 
        <FormModal ref={passwordModalRef} title={"Password Change"} formRef={null} recordLayout={{password:'',passwordConfirm:''}} idKey={""}>
        <Row>
        <Col md={7}>
        <TextInput label={"New Password"} type={"password"} rows={0} />
        <TextInput label={"Confirm New Password"} type={"password"} rows={0} />
        </Col>
        </Row>
        </FormModal>
        <ModalBox title={"Reset Board Configuration"}>
        <Row>
        <Col md={7}>
        <p>Resetting Board Configuration will remove board local WIFI authentication and board detail and deactivate board until reactivated</p>
        </Col>
        </Row>
        </ModalBox>
        <DeleteModal ref={deleteModalRef} title={"Delete Board"} deleteApi={"/board/"} param={board?.id||''} afterSubmit={deletehandle}>
        <p>Are you sure you want to delete {board?.name}?</p>
        <p>All devices and functionality associated will be lost</p>
        </DeleteModal>
        
        </Content>
        </div>
    )
}