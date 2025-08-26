'use client'
import { Component } from "react";
import { Row, Col, Form, Stack } from "react-bootstrap";
import TextInput from "./TextInput";
import {Form as formHandle} from "../form/Form";
type Props={
    seconds:boolean;
    mins:boolean;
    hour:boolean;
    type:string;
    name?:string;
    rows:number;
    required?:boolean;
    readOnly?:boolean;
    disable?:boolean;
    onChange?:any;
    warning?:string;
    value?:number;
    size?:any;
    md?:number;
    xs?:number;
    formRef?:formHandle|any;
}
type State={
    value:number;
    seconds:number;
    min:number;
    hour:number;
}
// time input that converts seconds,mins and hours to milliseconds
export default class TimeInput extends Component<Props,State>{
    
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
                hour:0
            };
    }
    componentDidMount(): void {
        if(this.props.value){
            var time=this.convertMiliSecondsToTime(this.props.value);
            this.value=this.props.value;
            this.seconds=time.seconds;
            this.min=time.minutes;
            this.hour=time.hours;
            this.setState({...this.state,value:this.props.value,seconds:time.seconds,min:time.minutes,hour:time.hours});
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
    public onChange(seconds:number,mins:number,hours:number){
        var value=0;
        if(this.props.seconds){
            value+=this.secondsToMills(seconds);
            seconds=seconds;
        }
        if(this.props.mins){
            value+=this.minsToMills(mins);
            mins=mins
        }
        if(this.props.hour){
            value+=this.hoursToMills(hours);
            hours=hours;
        }
        this.setState({...this.state,value:value,seconds:seconds,min:mins,hour:hours});
    }

    public secondsToMills(seconds:number){
        const millisecondsFromSeconds = seconds * 1000;
        return millisecondsFromSeconds;
    }
    public minsToMills(mins:number){
        const millisecondsFromMinutes = mins * 60 * 1000;
        return millisecondsFromMinutes;
    }
    public hoursToMills(hours:number){
        const miliSecondsFromHours=hours* 3600000;
        return miliSecondsFromHours;
    }
    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{"Time"}</Form.Label>
            <div className="mb-3">
            <Stack direction="horizontal" gap={2}>
            {this.props.seconds?
            <TextInput label={"Seconds"} type={"number"} rows={0} required={this.props.required} value={this.state.seconds} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(this.state.seconds||Number.parseInt(event.target.value),this.state.min,this.state.hour)}/>
            :null}
            {this.props.mins?
            <TextInput label={"Minutes"} type={"number"} rows={0} required={this.props.required} value={this.state.min} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(this.state.seconds,this.state.min||Number.parseInt(event.target.value),this.state.hour)}/>
            :null}
            {this.props.hour?
            <TextInput label={"Hours"} type={"number"} rows={0} required={this.props.required} value={this.state.hour} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(this.state.seconds,this.state.min,this.state.hour||Number.parseInt(event.target.value))}/>
            :null}
            </Stack>
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning} </Form.Text>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}