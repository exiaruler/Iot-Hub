'use client'
import { Component, ReactNode } from "react";
import { ButtonGroup } from "react-bootstrap";
import Group from "../Group";
interface Props{
    children?:ReactNode;
    backgroundColor?:any;
}
interface State{
    backgroundColour:any;
}
export default class Toolbar extends Component<Props,State>{
    constructor(props:Props) {
            super(props);
            this.state = {
                backgroundColour:'#F5F5F5'
            };
    }
    componentDidMount(): void {
        if(this.props.backgroundColor){
            this.setState({...this.state,backgroundColour:this.props.backgroundColor});
        }
    }
    render(){
        return(
            <Group backgroundColor={this.state.backgroundColour}>
            <ButtonGroup size={"sm"}>
            {this.props.children}
            </ButtonGroup>
            </Group>
        )
    }
}