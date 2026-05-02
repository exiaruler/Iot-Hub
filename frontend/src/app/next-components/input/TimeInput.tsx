'use client'
import { Row, Col, Form, Stack } from "react-bootstrap";
import TextInput from "./TextInput";
import InputBase from "@/component-base/InputBase";
import { InputInterface, State as BaseState } from "@/component-base/interface/input";
import Warning from "@/components/formGenComponents/Warning";
import { FormContext, RecordContext } from "@/components/form/FormHandle";
import { RegularButton } from "../buttons/RegularButton";

interface Props extends InputInterface{
    seconds:boolean;
    mins:boolean;
    hour:boolean;
}
interface State extends BaseState{
    value:number;
    seconds:number;
    min:number;
    hour:number;
    buttonName:string;
    millsDisplay:boolean;
}

// time input that converts seconds,mins and hours to milliseconds
export default class TimeInput extends InputBase{
    declare state:State;
    declare props:Props;
    static contextType = RecordContext;
    public value=0;
    public seconds=0;
    public min=0;
    public hour=0;
    
    constructor(props:Props) {
        super(props);
        this.state = {
                value:0,
                seconds:0,
                min:0,
                hour:0,
                warning:"",
                buttonName:"Show Mills",
                millsDisplay:false
        };
    }
    componentDidMount(): void {
        if(this.props.value){
            this.setTimeRead(this.props.value);
        }
        this.formHandleValueSet();
    }

    componentDidUpdate(prevProps: Readonly<InputInterface>, prevState: Readonly<State>, snapshot?: any): void {
        this.formHandleValueSetUpdate(prevProps);
    }
    public setTimeRead(timeValue:number,writeState:boolean=true):void{
        const time=this.convertMiliSecondsToTime(timeValue);
        this.value=timeValue;
        this.seconds=time.seconds;
        this.min=time.minutes;
        this.hour=time.hours;
        if(writeState){
            const state:State= {
                value:timeValue,
                seconds:time.seconds,
                min:time.minutes,
                hour:time.hours,
                warning:"",
                buttonName:this.state.buttonName,
                millsDisplay:this.state.millsDisplay
            };
            this.setState({...this.state,...state});
        }
    }
    public getTimeDisplay(section:string):number{
        let time=0;
        if(this.props.formRef){
            const form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                this.value=form.state.record[this.props.name];
                const obj=this.context as Record<string, any>;
                const timeObj=this.convertMiliSecondsToTime(parseInt(obj[this.props.name]));
                switch(section){
                    case "seconds":
                        time=timeObj.seconds;
                        break;
                    case "minutes":
                        time=timeObj.minutes;
                        break;
                    case "hours":
                        time=timeObj.hours;
                        break;
                }
            }
        }else if(this.props.value) {
            this.setTimeRead(this.props.value);
        }
        return time;
    }
    public getStateValue():any{
        if(this.props.formRef){
            const form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                this.setTimeRead(form.state.record[this.props.name],false);
                this.value=form.state.record[this.props.name];
                const context=this.context as FormContext;
                const rec=context.record;
                if(rec!=null){
                    return rec[this.props.name]||'';
                }
            }
        }else if(this.props.value) {
            this.setTimeRead(this.props.value);
        }
        return this.value;
    }
    public formHandleValueSet():void{
        if(this.props.formRef&&this.props.name){
            const curr=this.props.formRef.current;
            if(curr!=null&&this.warningComponent.current!=null) curr.addInput(this.warningComponent);
            if(curr!=null&&curr.props.record!=null&&Object.keys(curr.props.record).length>0){
                const val=curr.props.record[this.props.name];
                this.setTimeRead(val);
            }
            else if(curr!=null&&this.props.name)
            {
                const val=curr.props.recordLayout[this.props.name];
                this.setTimeRead(val);
            }
        }
    }
    
    // return error value
    public formHandleValueSetUpdate(prevProps: Readonly<InputInterface>):void{
        if(prevProps.formRef&&this.props.formRef&&this.props.name){
            const prevCurr=prevProps.formRef.current||this.props.formRef.current;
            const curr=this.props.formRef.current;
            if(JSON.stringify(curr.props.record)!=='{}'&&curr.props.record!=null){
                const pastId=prevCurr.state.id;
                const currId=curr.props.record[prevCurr.props.idKey];
                if(pastId!==currId){
                    const val=curr.props.record[this.props.name];
                    //this.setTimeRead(val);
                }

            }else{
                const val=curr.props.recordLayout[this.props.name];
                //this.setTimeRead(val);
            }
        }
    }
    
    public convertMiliSecondsToTime(mills:number): { hours: number; minutes: number; seconds: number }{
        const totalSeconds = Math.floor(mills / 1000);
        const seconds = totalSeconds % 60;
        const totalMinutes = Math.floor(totalSeconds / 60);
        const minutes = totalMinutes % 60;
        const hours = Math.floor(totalMinutes / 60);
        return { hours, minutes, seconds };
    }
    public onChangeTime(seconds:number, mins:number, hours:number, event:React.ChangeEvent<HTMLInputElement>):void{
        let value = 0;
        const stateCpy = { ...this.state };
        if(this.props.seconds){
            value += this.secondsToMills(seconds);
            stateCpy.seconds = seconds;
            this.seconds = seconds;
        }
        if(this.props.mins){
            value += this.minsToMills(mins);
            stateCpy.min = mins;
            this.min = mins;
        }
        if(this.props.hour){
            value += this.hoursToMills(hours);
            stateCpy.hour = hours;
            this.hour = hours;
        }

        this.value = value;
        stateCpy.value = value;
        this.setState(stateCpy);
        if(this.props.formRef){
            const form = this.props.formRef.current;
            form.onChangeRecord(this.props.name, value);
        }
        if(this.props.onChange){
            this.props.onChange(event);
        }
    }
     public onChange(event:React.ChangeEvent<HTMLInputElement>):void{
        const value=parseInt(event.target.value) || 0;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            const form=this.props.formRef.current;
            this.setTimeRead(value);
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(event);
        }
    }
    public millsFieldChange=()=>{
        if(!this.state.millsDisplay){
            this.setState(prevState => ({...prevState,millsDisplay:true,buttonName:"Show Formated Time"}));
        }else
        {
            this.setState(prevState => ({...prevState,millsDisplay:false,buttonName:"Show Mills"}));
        }
    }

    public secondsToMills(seconds:number):number{
        const millisecondsFromSeconds = seconds * 1000;
        return millisecondsFromSeconds;
    }
    public minsToMills(mins:number):number{
        const millisecondsFromMinutes = mins * 60 * 1000;
        return millisecondsFromMinutes;
    }
    public hoursToMills(hours:number):number{
        const miliSecondsFromHours=hours* 3600000;
        return miliSecondsFromHours;
    }
    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Stack direction="horizontal" gap={2}>
            <Stack hidden={this.state.millsDisplay} direction="horizontal" gap={1}>
            {this.props.hour?
            <TextInput label={"Hours"} type={"number"} rows={0} required={this.props.required} value={this.state.hour}
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeTime(this.state.seconds,this.state.min,Number.parseInt(event.target.value),event)}/>
            :null}
            {this.props.mins?
            <TextInput label={"Minutes"} type={"number"} rows={0} required={this.props.required} value={this.state.min} 
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeTime(this.state.seconds,Number.parseInt(event.target.value),this.state.hour,event)}/>
            :null}
            {this.props.seconds?
            <TextInput label={"Seconds"} type={"number"} rows={0} required={this.props.required} value={this.state.seconds} 
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeTime(Number.parseInt(event.target.value),this.state.min,this.state.hour,event)}/>
            :null}
            </Stack>
            <div hidden={!this.state.millsDisplay}>
            <TextInput label={'Mills Time'} type={"number"} rows={0} required={this.props.required} value={this.getStateValue()} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)} name={this.props.name}/>
            </div>
            <div>
            <RegularButton type="button" caption={this.state.buttonName} size={undefined} onClick={this.millsFieldChange}/>
            </div>
            </Stack>
            </div>
            <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}