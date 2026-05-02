'use client'
import BackButton from "@/app/next-components/buttons/BackButton";
import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import Form from "../../../../../next-components/form/Form";
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import { NextUIBase } from "@/NextUIBase";
import { Component, createRef, ReactElement, ReactNode, Ref, RefObject, useEffect, useRef, useState } from "react";
import { Col, Row } from "react-bootstrap";
import { findComponentFunction, findComponentMode } from "@/app/next-components/command-components/componentLibrary";
import React from "react";
import { useParams } from 'next/navigation';
import TextInput from "@/app/next-components/input/TextInput";
import NewButton from "@/app/next-components/form/NewButton";
import DeleteBox from "@/components/modal/DeleteBox";
import SaveButton from "@/app/next-components/buttons/SaveButton";
import SubForm from "@/app/next-components/form/SubForm";
import PinSubForm from "@/app/next-components/form/PinSubForm";
import ModeSubForm from "@/app/next-components/form/ModeSubForm";
import SelectInput from "@/app/next-components/input/SelectInput";
import Content, { ContentRef, ObjectArray, ObjectRecord } from "@/app/next-components/layout/Content";
interface ComponentRender{
    component:any;
    config:any;
}
interface Props{
    // new route
    newFunction:ObjectRecord;
    // new mode
    newMode:ObjectRecord;
    // route record
    record:ObjectRecord;
    // list of commands
    commands:ObjectArray;
    // board record
    board:ObjectRecord|null;
    // device record
    device:ObjectRecord|null;
    // electrodes component
    electrodes:Array<string>;
    // hardware
    boardHardware:ObjectRecord;
    
}
export default function Client({
    board,
    electrodes,
    device,
    newMode,
    newFunction,
    commands,
    record,
    boardHardware
}: Props) {
    const base = new NextUIBase();
    const params=useParams();
    let pinIndex=0;
    // Refs
    const modeLayoutRef = useRef<ObjectRecord>({});
    const boardTask = useRef<ObjectRecord>(null);
    const modeFormLay = useRef<Array<Record<string,any>>>([]);
    const functionFormLay = useRef<Array<Record<string,any>>>([]);
    const hardwareRef = useRef<ObjectRecord>(null);
    const boardRef = useRef<ObjectRecord>(null);
    const deviceRef = useRef<ObjectRecord>(null);
    const tabsRef = useRef<TabGroup>(null);
    const modeTabsRef = useRef<TabGroup|null>(null);
    const formRef = useRef<Form>(null);
    const formsCompRef= useRef<(Form)[]>([]);
    const modeTabCounter=useRef<number>(0);
    const modesTabFormRef=useRef<ReactNode[]>([]);
    const deleteModalRef=useRef<any>(null);
    const inputFields=useRef<Array<Component>>([]);
    const commandInputRef=useRef<SelectInput>(null);
    const contentRef=useRef<ContentRef>(null);
    // State
    const currentCommand=useRef<string>("");
    const [functionForm, setFunctionForm] = useState<ReactElement[]>([]);
    const [addMode, setAddMode] = useState(false);
    const [modeTabForm,setModeTabForm]=useState<ReactNode[]>([]);
    const [modeTabTitles,setModeTabTitles]=useState<string[]>([]);
    // Handlers
    const tabHandle = (tab: any) => {
        tabsRef.current?.handleTabSwitch(tab);
    };


    const getDeviceId = (): string => deviceRef.current?.deviceId || "";

    const modeHandleChange=(value:string,index:number)=>{
        const tabs=modeTabTitles;
        tabs[index]=value;
        setModeTabTitles([...tabs]);
    }
    const modeHandleAdd=(value:string)=>{
        const tabs=modeTabTitles;
        tabs.push(value);
        setModeTabTitles([...tabs]);
        return value;
    }
    const modeHandleRemove=(index:number)=>{
        const tabs=modeTabTitles;
        tabs.splice(index,1);
        setModeTabTitles([...tabs]);
    }
    const clearCommandSelection = () => {
        setFunctionForm([]);
        modesTabFormRef.current = [];
        setModeTabForm([]);
        modeTabCounter.current = 0;
        pinIndex = 0;
        functionFormLay.current = [];
        modeFormLay.current = [];
        currentCommand.current="";
        setAddMode(false);
        boardTask.current = null;
    };

    const currentCommandToJson=()=>{
        return JSON.parse(currentCommand.current)||null;
    }
    const addElement=(element:Component|null)=>{
        const content=contentRef.current;
        if(content){
            const fields=content.addInputRefComponent(inputFields,element);
            inputFields.current=fields.current;
        }
    }
    // render function form section
    const renderFunctionForm = () => {
        const layout = functionFormLay.current;
        const currentCommandObj=currentCommandToJson();
        let boardAct={...currentCommandObj?.boardCommand};
        let compArr=[];
        for(let i=0; i<layout.length;i++){
            const item=layout[i];
            const comp= item.component;
            const config={...item.config};
            if(config.subName&&currentCommandObj!=null){
                let moderec=null;
                let boardrec=null;
                let pinrec=null;
                if(record!=null){
                    moderec=record.mode[0];
                    boardrec=moderec.boardAction;
                    pinrec=boardrec.pins[config.index];
                }
                const modeComRef=createRef<Form>();
                const boardActionRef=createRef<Form>();
                const recLayout={...currentCommandObj?.boardCommand[config.name][config.index]};
                const subModeComRef=createRef<Form>();
                const recordLayNew={...newMode};
                let parentForm=<PinSubForm recordLayout={recordLayNew||{}}
                idKey="id"
                record={moderec}
                ref={modeComRef}
                objectKey={"mode"}
                index={0}
                formRef={formRef}
                array={true}
                >{<SubForm record={boardrec} recordLayout={boardAct||{}} idKey="id" ref={boardActionRef} objectKey={"boardAction"} formRef={modeComRef}>
                {
                    <SubForm record={pinrec} array={true} ref={subModeComRef} index={config.index}
                    objectKey={config.name} recordLayout={recLayout||{}} idKey={"id"} formRef={boardActionRef}>
                    {
                        React.createElement(comp,{...config,
                        formRef:subModeComRef,name:'pin',ref:(element:any)=>addElement(element)})
                    }
                    </SubForm>
                }    
                </SubForm>}</PinSubForm>
                compArr.push(parentForm);
                formsCompRef.current.push(modeComRef.current!);
                formsCompRef.current.push(boardActionRef.current!);
                formsCompRef.current.push(subModeComRef.current!);
            }else {
                let ele=React.createElement(comp,{...config,
                    formRef:formRef,
                    ref:(element:any)=>addElement(element)
                });
                compArr.push(ele);
            }
        }

        setFunctionForm(compArr);
    };

    // updates pins for the rest of the modes upon change
    const pinOnChange = () => {
        const mainForm=formRef.current;
        const rec=mainForm?.record;
        if(rec!=null){
            if(rec.mode.length>1){
                const modes=rec.mode;
                const firstMode=modes[0].boardAction.pins;
                for(let i=1; i<modes.length; i++){
                    const boardAct=modes[i].boardAction.pins;
                    for(let x=0; x<firstMode.length; x++){
                        boardAct[x].pin=firstMode[x].pin;
                    }
                    modes[i].boardAction.pins=boardAct;
                }
                mainForm?.onChangeRecord('mode',modes);
            }
        }
    };

    const removeMode = (index: number) => {
        index-=1;
        const modesTab=modesTabFormRef.current;
        const form=formRef.current;
        const modes:Record<string,any>[]=form?.getRecordValue('mode');
        modesTab.splice(index,1);
        modes.splice(index,1);
        setModeTabForm([...modesTab]);
        modesTabFormRef.current=[...modesTab];
        form?.onChangeRecord('mode',modes);
    };

    const deleteModeDisabled = () =>{
        if(modeTabForm.length < 2){
            return true;
        }
        return false;
    }

    const buildComponentConfig = (param: any, index: number) => {
        const config: any = { label: param.label };
        const subkey = param.subKey;

        if (param.component === "ElectrodeSelect") {
            config.options = electrodes;
            config.formRef = formRef;
            config.required = true;
            config.name = param.backgroundKey;
        }

        if (param.component === "PinSelect") {
            config.options = hardwareRef.current?.pins;
            config.displayKey = "boardPin";
            config.name = param.backgroundKey;
            config.subName = subkey;
            config.formRef = formRef;
            config.required = true;
            config.valueKey = "pin";
            config.onChange = () =>pinOnChange();
            config.index = pinIndex;
            pinIndex++;
        }


        return config;
    };

    const commandHandle = (value:string|number,recordLoad: boolean=false) => {
        if (!value) {
            clearCommandSelection();
            return;
        }
        const commandJson = commands.find((rec) => rec?.id == value)||null;
        if(commandJson!=null){
            if (!commandJson?.commandParameter.length) return;
            setAddMode(true);
            currentCommand.current=JSON.stringify(commandJson);
            const formParas = commandJson.commandParameter;
            boardTask.current = commandJson.boardCommand;
            
            const functionArr: ComponentRender[] = [];
            const modeArr: ComponentRender[] = [];
            const arrSubKeys: string[] = [];
            formParas.forEach((param:Record<string,any>, pinIndex:number) => {
                const comp = findComponentFunction(param.component);
                const compMode = findComponentMode(param.component);
                const config = buildComponentConfig(param, pinIndex);
                if (comp) {
                    functionArr.push({ component: comp.component, config });
                }

                if (compMode) {
                    let index=0;
                    if(arrSubKeys.indexOf(param.subKey)>-1){
                        index=arrSubKeys.length;
                    }
                    if(param.subKey!=""){
                        arrSubKeys.push(param.subKey);
                    }
                    const config:Record<string,any>={
                        label: param.label,
                        name: param.backgroundKey,
                        index: index,
                        subName: param.subKey,
                        required: true,
                        value: "",
                    };
                    if(param.component === "TimeInput"){
                        config.seconds=param.seconds;
                    }
                    modeArr.push({
                        component: compMode.component,
                        config: config,
                    });
                }
            });
            functionFormLay.current = functionArr;
            modeFormLay.current = modeArr;
            addModeForm(true,recordLoad);
        }else clearCommandSelection();
    }
    // add new mode to route form
    const addModeToForm=()=>{
        const form=formRef.current;
        const modes:Record<string,any>[]=form?.getRecordValue('mode')||record?.mode;
        let addMode={...newMode};
        const commandObj=currentCommandToJson();
        const boardAct={...commandObj.boardCommand};
        // set pins from first mode
        if(commandObj!=null&&addMode!=null&&modes.length>0){
            const firstModeBoardAct=modes[0]?.boardAction;
            if(firstModeBoardAct!=null){
                boardAct.pins=firstModeBoardAct.pins;
            }
            addMode.boardAction=boardAct;
        }else addMode.boardAction=boardAct;
        
        if(addMode!=null)modes.push(addMode);
        form?.onChangeRecord('mode',modes);

    }
    const addModeForm = (rewrite: boolean = false,recordLoad: boolean = false) => {
        if(recordLoad){
            createModeLoad(modeFormLay.current);
        }else {
            addModeToForm();
            createMode(modeFormLay.current);
        }
        if (rewrite) {
            renderFunctionForm();
        }
    };
    const tabModeDisplay=(ref:RefObject<Form|null>|null)=>{
        if(ref==null||ref.current==null) return "";
        const form=ref.current;
        const value=form.getRecordValue('mode');
        return value;
    }
    // cereate mode tab upon loaded function
    const createModeLoad=(componentsInput:Record<string,any>[])=>{
        let currentModes=modeTabCounter.current;
        const form=formRef.current;
        const modes:ObjectArray=form?.record?.mode;
        
        
        const commandObj=currentCommandToJson();
        let boardAct={...commandObj.boardCommand};
        const firstModeBoardAct=modes[0]?.boardAction;
        if(firstModeBoardAct!=null){
                boardAct.pins=firstModeBoardAct.pins;
        }
        // loop through mode to create mode tabs
        for(let ix=0; ix<modes.length;ix++){
            let compArr=[];
            const ref=createRef<Form>();
            const modeComRef=createRef<Form>();
            const modeRec=modes[ix];
            for(let i=0; i<componentsInput.length; i++){
                const component=componentsInput[i];
                const comp=component.component;
                const config=component.config;
                if(config.subName==""){
                    let ele=React.createElement(comp,{...config,formRef:modeComRef,key:i,rows:0});
                    compArr.push(ele);

                }else if(config.subName!=""&&commandObj!=null){
                    const recLayout={...commandObj.boardCommand[config.name][config.index]};
                    const subModeComRef=createRef<Form>();
                    let subForm=<SubForm array={true} ref={subModeComRef} index={config.index} record={modeRec?.boardAction[config.name][i]} objectKey={config.name} recordLayout={recLayout||{}} idKey={"id"} formRef={modeComRef}>
                        {
                            React.createElement(comp,{key:i,label:config.label,
                            name:config.subName,
                            rows:0,
                            formRef:subModeComRef,
                            ref:(element:any)=>addElement(element)})
                        }
                        </SubForm>;
                    compArr.push(subForm);
                    formsCompRef.current.push(subModeComRef.current!);
                }
        }
            const newModeLay={...newMode};
            newModeLay.boardAction=boardAct;
            const tabName=modeHandleAdd(modeRec?.mode)||newModeLay.mode
            const tab=<TabComponent key={currentModes} title={tabName} eventKey={currentModes}>
            <ModeSubForm recordLayout={newModeLay || {}}
            idKey="id"
            record={modeRec}
            ref={ref}
            objectKey={"mode"}
            index={currentModes}
            formRef={formRef}
            array={true}
            >
            <Row>
            <Col md={4} xs={8}>
            <TextInput
            formRef={ref}
            label="Mode"
            required={true}
            rows={0}
            onChange={(event:React.ChangeEvent<HTMLSelectElement>,num:number=(currentModes))=>modeHandleChange(event.target.value,num)}
            name="mode"/>
            <SubForm record={modeRec?.boardAction} id="BoardAct-Form" recordLayout={boardAct||{}} idKey="id" ref={modeComRef} objectKey={"boardAction"} formRef={ref}>
            {
                compArr
            }
            </SubForm>
            </Col>
            </Row>
            </ModeSubForm>
            <Row>
            <Col xs={16}>
            <RegularButton
            caption="Back"
            type="button"
            onClick={() => tabHandle("command")} size={undefined} />
            <SaveButton caption="Save Function" size={undefined} />
            <RegularButton
            caption="Delete Mode"
            type="button"
            disabled={modesTabFormRef.current.length<1}
            onClick={() => removeMode(currentModes)}
            size={undefined}/>
            <DeleteBox ref={deleteModalRef} title={"Delete"} deleteApi={"/board/"} baseUrl={base.util.baseURL+'/api'} param={board?.id||''}>
            <p>Are you sure you want to delete this function?</p>
            </DeleteBox>
            <RegularButton
            caption="Add Mode"
            type="button"
            onClick={() => addModeForm()} size={undefined}/>
            <NewButton onClick={()=>modeClear(modeComRef)} formRef={ref} caption="Clear Mode" size={undefined} />
            <NewButton formRef={formRef} caption="Clear" size={undefined} />
            </Col>
            </Row>
            </TabComponent>
            
            currentModes++;
            modeTabCounter.current=currentModes;
            formsCompRef.current.push(ref.current!);
            formsCompRef.current.push(modeComRef.current!);
            setModeTabForm((prev)=>[...prev,tab]);
            modesTabFormRef.current.push(tab);
        
        
        }
    }
    // create mode tab form
    const createMode=(componentsInput:Record<string,any>[])=>{
        let currentModes=modeTabCounter.current;
        const form=formRef.current;
        const modes:Record<string,any>[]=form?.record?.mode;
        const ref=createRef<Form>();
        const modeComRef=createRef<Form>();
        let compArr=[];
        const commandObj=currentCommandToJson();
        let boardAct={...commandObj.boardCommand};
        const firstModeBoardAct=modes[0]?.boardAction;
        if(firstModeBoardAct!=null){
                boardAct.pins=firstModeBoardAct.pins;
        }
        let modeLoop=1;
        for(let i=0; i<componentsInput.length; i++){
            const component=componentsInput[i];
            const comp=component.component;
            const config=component.config;
            if(config.subName==""){
                let ele=React.createElement(comp,{...config,formRef:modeComRef,key:i,rows:0,ref:(element:any)=>addElement(element)});
                compArr.push(ele);

            }else if(config.subName!=""&&commandObj!=null){
                const recLayout={...commandObj.boardCommand[config.name][config.index]};
                const subModeComRef=createRef<Form>();
                let subForm=<SubForm array={true} ref={subModeComRef} index={config.index} objectKey={config.name} recordLayout={recLayout||{}} idKey={"id"} formRef={modeComRef}>
                    {
                        React.createElement(comp,{key:i,label:config.label,
                        name:config.subName,
                        rows:0,
                        formRef:subModeComRef,
                        ref:(element:any)=>addElement(element)})
                    }
                    </SubForm>;
                compArr.push(subForm);
                formsCompRef.current.push(subModeComRef.current!);
            }
        }
        for(let x=1; x<=modeLoop;x++){
            const newModeLay={...newMode};
            newModeLay.boardAction=boardAct;
            const tab=<TabComponent key={currentModes} title={tabModeDisplay(ref)||newModeLay.mode} eventKey={currentModes}>
            <ModeSubForm recordLayout={newModeLay || {}}
            idKey="id"
            ref={ref}
            objectKey={"mode"}
            index={currentModes}
            formRef={formRef}
            array={true}
            >
            <Row>
            <Col md={4} xs={8}>
            <TextInput
            formRef={ref}
            label="Mode"
            required={true}
            rows={0}
            name="mode"/>
            <SubForm id="BoardAct-Form" recordLayout={boardAct||{}} idKey="id" ref={modeComRef} objectKey={"boardAction"} formRef={ref}>
            {
                compArr
            }
            </SubForm>
            </Col>
            </Row>
            </ModeSubForm>
            <Row>
            <Col xs={16}>
            <RegularButton
            caption="Back"
            type="button"
            onClick={() => tabHandle("command")} size={undefined} />
            <SaveButton caption="Save Function" size={undefined} />
            <RegularButton
            caption="Delete Mode"
            type="button"
            disabled={modesTabFormRef.current.length<1}
            onClick={() => removeMode(currentModes)}
            size={undefined}/>
            <DeleteBox ref={deleteModalRef} title={"Delete"} deleteApi={"/board/"} baseUrl={base.util.baseURL+'/api'} param={board?.id||''}>
            <p>Are you sure you want to delete this function?</p>
            </DeleteBox>
            <RegularButton
            caption="Add Mode"
            type="button"
            onClick={() => addModeForm()} size={undefined}/>
            <NewButton onClick={()=>modeClear(modeComRef)} formRef={ref} caption="Clear Mode" size={undefined} />
            <NewButton formRef={formRef} caption="Clear" size={undefined} />
            </Col>
            </Row>
            </TabComponent>
            
            currentModes++;
            modeTabCounter.current=currentModes;
            formsCompRef.current.push(ref.current!);
            formsCompRef.current.push(modeComRef.current!);
            setModeTabForm((prev)=>[...prev,tab]);
            modesTabFormRef.current.push(tab);
        }
    }
    const modeClear=(comRef:RefObject<SubForm|null>)=>{
        const ref=comRef.current;
        if(ref){
            ref.newRecord();
        }
    }
    const submitHandle=async()=>{
        const form = formRef.current;
        const response=await form?.response;
        if(response.status===200)contentRef.current!.router.push('/boards/'+params?.board+'?device='+device?.name+"&tab=functions");
    }
    
    const loadData = (
        _commands: ObjectArray,
        boardHardware: ObjectRecord,
        board: ObjectRecord,
        device: ObjectRecord,
        _functionLayout: ObjectRecord,
        modeLayout: ObjectRecord,
        recordData: ObjectRecord
    ) => {
        hardwareRef.current = boardHardware;
        boardRef.current = board;
        deviceRef.current = device;
        modeLayoutRef.current = modeLayout;
        // If a record is provided, ensure the form reflects it (select command, render modes, etc.)
        if (recordData != null) {
            clearCommandSelection();
            commandHandle(recordData.commandId,true);
        }
    };

    useEffect(() => {
        loadData(
            commands,
            boardHardware,
            board?.board,
            device,
            newFunction,
            newMode,
            record
        );
    }, [commands, board, device, record]);

    return (
        <div>
            <Content ref={contentRef}>
            <Row>
                <Col md={2}>
                    <BackButton url={'/boards/'+params?.board+'?device='+device?.name+"&tab=functions"} />
                </Col>
                <Col xs={9}>
                    <Form
                        onSubmit={submitHandle}
                        post={`/route/add-route-socket/${getDeviceId()}`}
                        put="/route/update-route-socket/"
                        recordLayout={newFunction || {}}
                        ref={formRef}
                        record={record}
                        idKey="id"
                    >
                        <TabGroup ref={tabsRef} defaultActiveKey="command">
                            <TabComponent title="Function" eventKey="command">
                                <Row>
                                    <Col md={4}>   
                                        <SelectInput
                                            ref={commandInputRef}
                                            formRef={formRef}
                                            label="Command"
                                            name="commandId"
                                            valueKey="id"
                                            displayKey="displayName"
                                            options={commands}
                                            required
                                            onChange={(e: React.ChangeEvent<HTMLSelectElement>) => commandHandle(e.target.value)}
                                            rows={0}
                                        />
                                        {functionForm.length > 0?
                                        <TextInput
                                            formRef={formRef}
                                            ref={(element)=>addElement(element)}
                                            required
                                            label="Function Name"
                                            name="route"
                                            rows={0}
                                        />
                                        :null}
                                    </Col>
                                    <Col md={4}>{functionForm}</Col>
                                </Row>
                                <Row>
                                    <Col md={2}>
                                        {!addMode && (
                                            <RegularButton
                                                caption="Save Function"
                                                disabled={currentCommand.current==""} size={undefined}                                            />
                                        )}
                                        {addMode && (
                                            <RegularButton
                                                caption="Next"
                                                type="button"
                                                onClick={() => tabHandle("mode")} size={undefined}                                            />
                                        )}
                                    </Col>
                                </Row>
                            </TabComponent>

                            {addMode && (
                                <TabComponent title="Mode" eventKey="mode">
                                    <TabGroup defaultActiveKey={0} ref={modeTabsRef}>
                                        {modesTabFormRef.current.map((tab: ReactNode, currentModes) =>
                                            React.isValidElement(tab)
                                                ? React.cloneElement(tab as React.ReactElement<any>, {
                                                    eventKey: currentModes,
                                                    tabKey: currentModes,
                                                  })
                                                : tab
                                        )}
                                    </TabGroup>
                                </TabComponent>
                            )}
                        </TabGroup>
                        
                    </Form>
                </Col>
            </Row>
            </Content>
        </div>
    );
}
