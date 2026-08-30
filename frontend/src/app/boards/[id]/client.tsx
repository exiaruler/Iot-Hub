'use client'
import ModalBox from "@/components/modal/ModalBox"
import { RegularButton } from "@/app/next-components/buttons/RegularButton"
import TextInput from "@/app/next-components/input/TextInput"
import TabComponent from "@/components/Tab/TabComponent"
import TabGroup from "@/components/Tab/TabGroup"
import { Row, Col, Stack } from "react-bootstrap"
import { useEffect, useRef, useState } from "react"
import ConfirmButton from "@/components/Buttons/ConfirmButton"
import AddForm from "./add-form"
import FormModal from "@/app/next-components/modal/FormModal"
import ModalButton from "@/components/Buttons/ModalButton"
import ConfigForm from "./config-form"
import { ContentRef, ObjectArray, ObjectRecord } from "@/app/next-components/layout/Content"
import Content from "@/app/next-components/layout/Content"
import Dev from "@/app/next-components/user/dev"
import DeleteModal from "@/app/next-components/modal/DeleteModal"
import Device from "./device"
import BackButton from "@/app/next-components/buttons/BackButton"

interface Props{
    deviceForm:ObjectRecord;
    boardForm:ObjectRecord;
    board:ObjectRecord;
    boardHardware:ObjectRecord;
    query:ObjectRecord;
}
// board page
export default function Client(props:Props){
    const contentRef=useRef<ContentRef>(null);
    const passwordModalRef=useRef<FormModal>(null);
    const deleteModalRef:any=useRef(null);
    const updateModalRef=useRef<ModalButton>(null);
    const tabGrpRef=useRef<TabGroup>(null);
    const formRefs:any=useRef([]);
    const [activated,setActivated]=useState(true);
    const [board,setBoard]=useState<ObjectRecord>(props.board);
    const [devices,setDevices]=useState<ObjectArray>(props.board?.device);
    const [device,setDevice]=useState<ObjectRecord>(null);
    const [hardware,setHardware]=useState<ObjectRecord>(props.boardHardware);
    // use to store next update occurance
    const nextUpdateDt=useRef<Date>(null);
    
    const openChangePass=()=>{
        passwordModalRef.current?.open();
    }
    const openDelete=()=>{
        deleteModalRef.current?.open();
    }
    
    const loadForms=(forms:ObjectRecord)=>{
        formRefs.current.push(forms);
    }
    const loadDevice=()=>{
        const content=contentRef.current;
        const device=content?.getQueryField('device');
        const tabs=tabGrpRef.current;
        if(device!=null){
            const dev=device;
            if(dev!=null){
                let index=devices.findIndex((d:ObjectRecord)=>d?.name==dev);
                if(index>-1)tabs?.handleTabSwitch(index);
            }

        }
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
    const deleteFunctionAfterHandle=(deviceIndex:number,id:number)=>{
        const arr=[...devices];
        const funcArr=arr[deviceIndex]?.routes;
        funcArr.splice(funcArr.findIndex((func:Record<string,any>)=>func.id==id),1);
        setDevices(arr);
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
        if(record==null) return;
        const devs=[...(devices ?? [])];
        devs.push(record);
        setDevices(devs);
    }
    const showDate=(dateTime:Date|null)=>{
        if(dateTime==null) return "";
        return new Date(dateTime).toDateString();
    }
    const showTime=(dateTime:Date|null)=>{
        if(dateTime==null) return "";
        const dt= new Date(dateTime);
        const time=dt.toLocaleTimeString();
        return time;
    } 
    const goToQueue=()=>{
        const content=contentRef.current;
        content?.router.push('/boards/queue/'+board?.boardId);
    }
    const goToTasks=()=>{
        const content=contentRef.current;
        content?.router.push('/boards/task-queue/'+board?.boardId);
    }
    const nextUpdate=async ()=>{
        const content=contentRef.current;
        if(!content || !board?.boardId) return;

        const nextOp=nextUpdateDt.current;
        const boardMillis=board?.millis||0;
        if(nextOp!=null&&activated&&boardMillis>0 && content?.passDateTime(nextOp)){
            const request=await content.util.fetchClientQuery('/board/get-board-id/'+board.boardId,'GET');
            if(request.status==200){
                const data=request?.json ?? {};
                setBoard((prev)=>({ ...(prev ?? {}), ...data }));
                setDevices(data.device);
                nextUpdateDt.current=data?.nextQueueOperation;
            }
        }
    }
    useEffect(()=>{
        const timer=window.setInterval(nextUpdate,1000);
        return ()=>window.clearInterval(timer);
    },[activated, board?.boardId, board?.nextQueueOperation, board?.millis]);

    useEffect(()=>{
        nextUpdateDt.current=board?.nextQueueOperation;
        loadForms(props.deviceForm);
        boardActive();
        loadDevice();
    },[])
    return(
        <div>
        <Content ref={contentRef}>
        <Row>
        <Col md={2} xs={2}>
        <BackButton url={"/boards"}/>
        </Col>
        <Col md={10} xs={14}>
        <TabGroup defaultActiveKey={"board"} ref={tabGrpRef} onSelect={handleTabSelect}>
        <TabComponent title={board?.name||''} eventKey={"board"}>
        <div>
        <Row>
        <Col md={3} xs={9}>
        <TextInput label={"Board ID"}  rows={0} value={board?.boardId} readOnly={true}/>
        
        <TextInput label={"Version"} value={board?.firmwareVersion} readOnly={true} rows={0}/>
        <TextInput label={"Local IP"}  rows={0} value={board?.ip} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <TextInput label={"RAM Usage"}  rows={0} value={board?.ramUsage} readOnly={true}/>
        <TextInput label={"Total RAM"}  rows={0} value={hardware?.maxRam} readOnly={true}/>
        <TextInput label={"Last Operation Time"}  rows={0} value={board?.millis} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <TextInput label={"Status"}  rows={0} value={status(board?.activated)} readOnly={true}/>
        <TextInput hidden={true} label={"Routine Check"}  rows={0} value={board?.periodicCheck} readOnly={true}/>
        <TextInput label={"Next Update Date"}  rows={0} value={showDate(board?.nextQueueOperation)} readOnly={true}/>
        <TextInput label={"Next Update Time"}  rows={0} value={showTime(board?.nextQueueOperation)} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <TextInput label={"Last Connection Date"}  rows={0} value={showDate(board?.lastConnectDateTime)} readOnly={true}/>
        <TextInput label={"Last Connection Time"}  rows={0} value={showTime(board?.lastConnectDateTime)} readOnly={true}/>
        </Col>
        </Row>
        <Row>
        <Col md={9} xs={12}>
        <Stack direction="horizontal" gap={2} className="mt-3">
        <RegularButton caption={"Operations"} size={undefined} onClick={goToQueue} disabled={!activated}/>
        <RegularButton caption={"Board Tasks"} disabled={!activated} onClick={goToTasks}/>
        <ModalButton disabled={!activated} buttonCaption={"More Details"} title={"Board Information"}>
        <Row>
        <Col md={6} xs={7}>
        <TextInput label={"Last Heap"}  rows={0} value={board?.heap} readOnly={true}/>
        <TextInput label={"Heap Total"}  rows={0} value={board?.heapTotal} readOnly={true}/>
        <TextInput label={"SSID"}  rows={0} value={board?.ssid} readOnly={true}/>
        <TextInput label={"Mac Address"}  rows={0} value={board?.macAddress} readOnly={true}/>
        </Col>
        <Col md={6} xs={7}>
        <TextInput label={"Board Model"}  rows={0} value={hardware?.boardName} readOnly={true}/>
        <TextInput label={"Created Date"}  rows={0} value={showDate(board?.createdDate)} readOnly={true}/>
        <TextInput label={"Login Date"}  rows={0} value={showDate(board?.lastLoginDateTime)} readOnly={true}/>
        <TextInput label={"Login Time"}  rows={0} value={showTime(board?.lastLoginDateTime)} readOnly={true}/>
        <TextInput label={"Activated Date"}  rows={0} value={showDate(board?.activatedDateTime)} readOnly={true}/>

        </Col>
        </Row>
        </ModalButton>
        <ConfirmButton disabled={!activated} buttonCaption={"Reset"} title={"Reset Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('restart')}>
        <p>Are you sure you want to restart board?</p>
        </ConfirmButton>
        <ConfirmButton disabled={!activated} buttonCaption={"Update"} title={"Update Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('update')}>
        <p>Are you sure you want to update?</p>
        </ConfirmButton>
        <ConfirmButton disabled={!activated} buttonCaption={"Reset Board Configuration"} title={"Reset Board Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('restart-board-config')}>
        <p>Are you sure you want to reset board configurations?</p>
        <p>Board configurations will be wiped and board will be deactivated until reactivated</p>
        </ConfirmButton>
        </Stack>
       <Stack direction="horizontal" gap={2} className="mt-3">
        <RegularButton caption={"Change Password"} onClick={openChangePass}/>
        <ModalButton ref={updateModalRef} buttonCaption={"Configurations"} title={"Configured Board"} submitCaption={"Save"}>
        <ConfigForm activated={activated} formLayout={props.boardForm} submissionHandle={handleUpdate} record={board} modalRef={updateModalRef}/>
        </ModalButton>
        <Dev>
        <ConfirmButton disabled={!activated} buttonCaption={"Upload"} title={"Upload Confirmation"} submitCaption={"Confirm"} submit={()=>boardCommand('update')}>
        <p>Are you sure you want to upload?</p>
        <p>Board will no longer receive commands from the Proto until new firmware is uploaded from IDE or manual reset</p>
        </ConfirmButton>
        </Dev>
        <RegularButton caption={"Delete Board"} size={undefined} type={undefined} onClick={openDelete}/>
       </Stack>
        </Col>    
        </Row>
        </div>
        </TabComponent>
        {
            devices.map((dev:ObjectRecord,key:number)=>(
                <TabComponent title={dev?.name} eventKey={key} disabled={!activated}>
                <Device key={key} index={key} active={false} device={dev} board={board} deleteDeviceMethod={deviceDeleteHandle} deleteFunctionMethodAfter={deleteFunctionAfterHandle}/>                
                </TabComponent>   
            ))
        }
        <TabComponent title={"Add Device"} eventKey={"add"} disabled={!activated}>
        <AddForm onUpdate={handleAddDevice} boardId={board?.id.toString() || ""} formLayout={props.deviceForm}/>
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