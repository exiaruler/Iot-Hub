'use client'
import BackButton from "@/app/next-components/buttons/BackButton";
import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import CurrentInput from "@/app/next-components/command-components/CurrentInput";
import ElectrodeSelect from "@/app/next-components/command-components/ElectrodeSelect";
import PinSelect from "@/app/next-components/command-components/PinSelect";
import FormHandle from "@/components/form/FormHandle";
import Form from "../../../../../next-components/form/Form";
import FormGenSelect from "@/components/formGenComponents/FormGenSelect";
import { FormGenText } from "@/components/formGenComponents/FormGenText";
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import { NextUIBase } from "@/NextUIBase";
import { ReactNode, useEffect, useRef, useState } from "react";
import { Col, Row } from "react-bootstrap";
import { Component, findComponentFunction, findComponentMode } from "@/app/next-components/command-components/componentLibrary";
import React from "react";
import { useRouter } from 'next/navigation';
import SaveButton from "@/app/next-components/buttons/SaveButton";
import TextInput from "@/app/next-components/input/TextInput";
interface ComponentRender{
    component:any;
    config:any;
}
interface Props{
    newFunction:Object;
    newMode:Object;
    record:Object;
    commands:Array<Object>;
    board:any;
    device:Object;
    electrodes:Array<string>;
    
}
export default function Client({board,electrodes,device,newMode,newFunction,commands,record}:Props){
    const base=new NextUIBase();
    const router = useRouter();
    // mode json template
    const modeLayoutRef:any=useRef({});
    // form
    const [functionRecord,setFunctionRecord]:any=useState({});
    const [modeRecords,setModeRecords]=useState([]);
    const modeRecordsTaskRef:Array<Object>=useRef([]);
    // pins reference
    const pinsUsedRef=useRef([]);
    // command list
    //const [commands,setCommands]:any=useState([]);
    // command selected
    const [command,setCommand]:any=useState(null);
    // board task
    const boardTask=useRef(null);
    // function form setup
    const [functionForm,setFunctionForm]:Array<any>=useState([]);
    // mode form layout
    const modeFormLay:any=useRef([]);
    const functionFormLay:any=useRef([]);
    // mode form setup
    const [modeForms,setModeForms]:any=useState([]);
    // mode tab display
    const [addMode,setAddMode]=useState(false);
    // hardware model
    const hardwareRef:any=useRef(null);
    // data references
    const boardRef:any=useRef(null);
    const deviceRef=useRef(null);
    // component ref
    const tabsRef:any=useRef(null);
    const modeTabsRef:any=useRef(null);
    const formRef:any=useRef(null);
   
    const tabHandle=(tab:any)=>{
        tabsRef.current.handleTabSwitch(tab);
    }
  
    const commandHandle=(value:any,existFunctionRec:Object|null)=>{
        //var value=event.target.value;
        if(value!=""){
            const com=JSON.stringify(commands.find((rec:any)=>rec.id==value));
            var commandJson=JSON.parse(com);
            const taskTempl=commandJson?.boardCommand;
            //taskTempl.id=0;
            if(commandJson?.commandParameter.length>0){
                clearCommandSelection();
                setAddMode(true);
                setCommand(commandJson);
                setFunctionRecord({...functionRecord,'commandId':value});
                var formParas=commandJson?.commandParameter;
                boardTask.current=taskTempl;
                // render fields for that command
                if(formParas.length>0){
                    var functionArr=[];
                    var modeArr=[];
                    var pinIndex=0;
                    // function tab
                    for(var i=0; i<formParas.length; i++){
                        var comp=findComponentFunction(formParas[i].component);
                        var compMode=findComponentMode(formParas[i].component);
                        var options=[];
                        var config:any={};
                        config.label=formParas[i].label;
                        const subkey=formParas[i].subKey;
                        if(comp!=null||comp!=undefined){
                            if(formParas[i].component=="ElectrodeSelect"){
                                options=electrodes;
                                config.options=options;
                                config.formRef=formRef;
                                config.required=true;
                                if(existFunctionRec!=null){
                                    config.value=existFunctionRec?.electrode||functionRecord?.electrode;
                                }
                                config.name=formParas[i]?.backgroundKey;
                                //config.onChange=(event:any)=>setFunctionRecord({...functionRecord,[event.target.name]:event.target.value});
                            }
                            if(formParas[i].component=="PinSelect"){
                                options=hardwareRef.current?.pins;
                                config.options=options;
                                config.displayKey="boardPin";
                                config.name=formParas[i]?.backgroundKey;
                                config.subName=subkey;
                                config.required=true;;
                                config.valueKey="pin";
                                const capturePin=pinIndex;
                                config.onChange=(event:any)=>pinOnChange(event.target.name,subkey,event.target.value,capturePin);
                                config.pinIndex=pinIndex;
                                if(existFunctionRec!=null){
                                    const boardAct=existFunctionRec.mode[0].boardAction;
                                    if(boardAct!=null){
                                        var pinVal=boardAct.pins[pinIndex][subkey];
                                        config.value=pinVal
                                    }
                                }
                                //config.value=14;
                                pinIndex++;
                            }
                            const functionComp:ComponentRender={
                                component: comp.component,
                                config: config
                            };
                            functionArr.push(functionComp);
                        }
                        if(compMode!=null||compMode!=undefined){
                            var label=formParas[i].label;
                            var modeConfig:any={label:label,name:formParas[i]?.backgroundKey,index:0,onChange:null,subName:formParas[i]?.subKey,required:true};
                            const modeComp:ComponentRender={
                                component:compMode.component,
                                config:modeConfig
                            }
                            modeArr.push(modeComp);
                        }
                    }
                    //setFunctionForm(functionArr);
                    functionFormLay.current=functionArr;
                    modeFormLay.current=modeArr;
                    
                    //if(existFunctionRec!=null){
                        addModeForm(true);
                    //}
                    
                }

            }else
            {

            }
            console.log(commandJson);
        }else
        {
            clearCommandSelection();
        }
    }

    const clearCommandSelection=()=>{
        setFunctionRecord({...functionRecord,command:null});
        setFunctionForm([]);
        setModeForms([]);
        setModeRecords([]);
        modeFormLay.current=[];
        setCommand(null);
        setAddMode(false);
        boardTask.current=null;
    }
    const renderFunctionForm=()=>{
        var layout:Array<ComponentRender>=functionFormLay.current;
        var formState=[];
        for(var i=0; i<layout.length; i++){
            formState.push(React.createElement(layout[i].component,layout[i].config));
        }
        setFunctionForm(formState);
    }
    const addModeForm=(rewrite:Boolean=false,modeRecord:Object|null)=>{
        var modeTot=modeRecords.length;
        if(modeTot===0) {
            modeTot=1;
        }else modeTot++;
        var value="New Mode";
        var modeRec={...modeLayoutRef.current};
        const boardTsk={...boardTask.current};
        if(pinsUsedRef.current.length>0){
            boardTsk.pins=pinsUsedRef.current;
        }
        modeRec.boardAction=boardTsk;
        modeRec.mode=value;
        const task=boardTsk;
        var firstMode:Array<ReactNode>=[];
        var layout=modeFormLay.current;
        var currComp=null;
        let indexCount=0;
        for(var i=0; i<layout.length; i++){
            var lay=layout[i];
            if(lay.component!=currComp){
                currComp=lay.component;
                indexCount=0;
            }else indexCount++;
            const captureIndex=indexCount;
            console.log(lay.config);
            if(modeRecord!=null){
                const functionRec=modeRecord[lay.config.name];
                if(lay.config.subName!=""){
                    debugger
                    console.log(modeRecords);
                    const value=functionRec[captureIndex][lay.config.subName];
                    console.log(value);
                    lay.config={...lay.config,value:modeRecord[lay.config.name][captureIndex][lay.config.subName]};
                }
                console.log(modeRecord);
            }
            lay.config.onChange=(event:any)=>commandOnChange(modeRecords.length,event.target.name,lay.config.subName,event.target.value,captureIndex);
            firstMode.push(React.createElement(lay.component,lay.config));
        }
        if(!rewrite){
            const existFormArr=[...modeForms];
            existFormArr.push(firstMode);
            setModeForms((prev:any)=>[...modeForms,firstMode]);
            const arr:any=[...modeRecords];
            arr.push(modeRec);
            setModeRecords(arr);
            var tasks=[...modeRecordsTaskRef.current];
            tasks.push(task);
            modeRecordsTaskRef.current=tasks;
            console.log(modeRecordsTaskRef.current);
        }else{
            const existFormArr=[];
            existFormArr.push(firstMode);
            setModeForms((prev:any)=>[firstMode]);
            const arr:any=[];
            arr.push(modeRec);
            setModeRecords((prev:any)=>arr);
            renderFunctionForm();
            modeRecordsTaskRef.current=[task];
        }
    }
    // update mode record 
    const modeOnChange=(index:number,name:string,value:any)=>{
        const update=modeRecords.map((rec:any,i)=>i==index
        ?{...rec,[name]:value}:rec);
        setModeRecords(update);
    }
    // update command task
    const commandOnChange=(modeIndex:number,name:string,subName:string="",value:any,arrayIndex:number=-1)=>{
        debugger;
        const functionRecs=[...modeRecordsTaskRef.current];
        const rec={...functionRecs[modeIndex]};
        if(name!=""){
            if(subName!=""){
                const recArr=[...rec[name]];
                if(arrayIndex>-1){
                    const arrRec={...recArr[arrayIndex]};
                    arrRec[subName]=value;
                    recArr[arrayIndex]=arrRec;
                    rec[name]=[...recArr];
                }
            }else{
                rec[name]=value;
            }
        }
        modeRecordsTaskRef.current[modeIndex]=rec;
    }
    const pinOnChange=(name:string,subName:string="",value:number,arrayIndex:number=-1)=>{
        const rec=[...modeRecordsTaskRef.current];
        const firstRec={...rec[0]};
        const pins=[...firstRec[name]];
        const pinRec={...pins[arrayIndex]};
        pinRec[subName]=value;
        pins[arrayIndex]=pinRec;
        firstRec[name]=pins;
        pinsUsedRef.current=pins;
        for(var i=0; i<rec.length; i++){
            modeRecordsTaskRef.current[i][name]=pins;
        }
    }
    const removeMode=(index:number)=>{
        var prev=index--;
        //var tabs=modeTabsRef.current.onSelectTab(prev);
        const arrRec=[...modeRecords];
        const arrForm=[...modeForms];
        arrRec.splice(index,1);
        arrForm.splice(index,1);
        setModeRecords(arrRec);
        //setModeForms(arrForm);
    }
    const renderModeForm=(array:Array<any>,index:number)=>{
        return array;
    }
    const deleteModeDisabled=()=>{
        var res=false;
        if(modeForms.length<2) res=true;
        return res;
    }
    const submit=async()=>{
        var form=formRef.current;
        // combined function and modes
        var functionRec=form.getRecord();
        var modesRec=modeRecords;
        var modeTasksRec=modeRecordsTaskRef.current;
        for(var i=0; i<modesRec.length; i++){
            modesRec[i].boardAction=modeTasksRec[i];
        }
        functionRec.mode=modesRec;
        const deviceId=deviceRef.current.deviceId;
        var save=false;
        debugger;
        if(functionRec?.id>0){
            // PUT

        }else
        {
            // post to API
            const request=await base.util.fetchClient('/function/'+deviceId,'POST',functionRec);
            const status=request.status;
            if(status==200){
                save=true;
                router.push('/boards/'+boardRef.current.boardId);
            }

        }
        console.log(functionRec);
        console.log(modeRecords);
        console.log(modeRecordsTaskRef.current);
        if(save) router.push('/boards/'+boardRef.current.boardId);
    }
    const loadData=(commands:Array<any>,hardware:any,board:any,device:any,functionLayout:Object,modeLayout:Object,functionRec:Object)=>{
        hardwareRef.current=hardware;
        boardRef.current=board;
        deviceRef.current=device;
        if(functionRec==null){
            setFunctionRecord(functionLayout);
        }else {
            // load function
            var comm=null;
            if(functionRec.modes){
                const firstMode=functionRec.mode[0].boardAction.method;
                comm=commands.find(com=>com.command==firstMode);
            }else{
                comm=commands.find(com=>com.command==functionRec.boardAction.command);   
            }
            //debugger;
            commandHandle(comm.id,functionRec);
            setFunctionRecord(functionRec);
            var tskArr=[];
            for(var i=0; i<functionRec.mode.length; i++){
                tskArr.push(functionRec.mode[i].boardAction);
                if(i==0){
                    addModeForm(false,functionRec.mode[i].boardAction);
                }else addModeForm(false,functionRec.mode[i].boardAction);
                    
            }
            modeRecordsTaskRef.current=tskArr;
            setModeRecords(functionRec?.mode);
            //debugger;
            //modeRecordsTaskRef
            
        }
        modeLayoutRef.current=modeLayout;
    }
    useEffect(()=>{
        loadData(commands,board.hardware,board.board,device,newFunction,newMode,record);
    },[commands,boardRef,deviceRef]);
    return(
        <div>
        <Row>
        <Col md={2}>
        <BackButton url={"/boards/"+boardRef.current?.boardId}/>
        </Col>
        <Col xs={9}>
        <Form recordLayout={newFunction} ref={formRef} onSubmit={submit} formControlOverride={true} record={functionRecord}>
        <TabGroup ref={tabsRef} defaultActiveKey={"command"}>
        <TabComponent title={"Function"} eventKey={"command"}>
        <Row>
        <Col md={4}>
        <TextInput formRef={formRef} required={true} label={"Function Name"} name="route" type={""} rows={0} value={functionRecord?.route} onChange={(event:any)=>base.onChange(event.target.name,event.target.value,setFunctionRecord,functionRecord)}/>
        <FormGenSelect value={functionRecord.commandId} formRef={formRef} label={"Command"}  name={"commandId"} valueKey={"id"} displayKey={"displayName"} options={commands} onChange={(event:any)=>commandHandle(event.target.value)} required={true}/>
        </Col>
        <Col md={4}>
        {
            functionForm
        }
        </Col>
        </Row>
        <Row>
        <Col md={2}>
        {!addMode?
            <RegularButton caption={"Save Function"} size={undefined} type={undefined} disabled={command==null}/>
        :null}
        {addMode?
        <RegularButton caption={"Next"} size={undefined} type={'button'} onClick={()=>tabHandle("mode")}/>
        :null}
        </Col>
        </Row>  
        </TabComponent>
        {addMode?
            <TabComponent  title={"Mode"} eventKey={"mode"}>
            <TabGroup defaultActiveKey={0} ref={modeTabsRef}>
            {
                modeForms.map((arr:any,index:number)=>(
                    <TabComponent title={modeRecords[index].mode} eventKey={index}>
                    <Row>
                    <Col md={4} xs={8}>
                    <FormGenText label={"Mode"} type={""} rows={0} name={'mode'} value={modeRecords[index].mode} onChange={(event:any)=>modeOnChange(index,event.target.name,event.target.value)} />
                    {
                        renderModeForm(arr,index)
                    }
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={16}>
                    <RegularButton caption={"Back"} size={undefined} type={"button"} onClick={()=>tabHandle("command")}/>
                    <SaveButton caption={"Save Function"} size={undefined} type={undefined} />
                    <RegularButton caption={"Delete"} size={undefined} type={"button"} onClick={()=>removeMode(index)} disabled={false}/>
                    <RegularButton caption={"Add Mode"} size={undefined} type={"button"} onClick={()=>addModeForm()}/>
                    </Col>
                    </Row>   
                    </TabComponent>
                ))
                
            }
            </TabGroup>    
            </TabComponent>
        :null}
        </TabGroup>
        </Form>
        </Col>
        </Row>
        </div>
    );
}