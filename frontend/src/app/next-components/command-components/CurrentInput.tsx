'use client'
import { FormGenText } from "@/components/formGenComponents/FormGenText"
import { Col, Form, Row } from "react-bootstrap";
import { Props,State } from "@/components/formGenComponents/interface/textInterface";
import { Component } from "react";
export default class CurrentInput extends Component<Props,State>{
    constructor(props:Props) {
            super(props);
            this.state = {
                value:0
            };
    }
    componentDidMount(): void {
        //debugger;
        //if(this.props.value){
            this.setState({...this.state,value:this.props.value});
        //}
    }
    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any): void {
        //debugger;
        
        if(this.props.value){
            this.setState({...this.state,value:this.props.value});
        }
        
        console.log(prevState);
    }
    onChange(event:any){
        //console.log(target);
        if(this.props.onChange){
            this.props.onChange(event);
        }
    }
    inputHandle(value:number){
        //console.log(value);
        if(value>255){
            this.setState({...this.state,value:0});
        }
    }
     render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <div>
            <Form.Control readOnly={this.props.readOnly} disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Text"} required={this.props.required} onChange={(event:any)=>this.onChange(event)}  name={this.props.name} type={"number"} defaultValue={this.props.value}
            onKeyDown={(event:any)=>this.inputHandle(event.target.value)}    />
            </div>
            <div>
            
            </div>
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning} </Form.Text>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}