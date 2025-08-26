'use client'
import { Tab, TabsProps } from "react-bootstrap";
import TabProps from './TabProps';
import { Component } from "react";
/*
export default function TabComponent(props:TabProps){
    
    return(
        <Tab title={props.title} eventKey={props.eventKey}>
            {
                props.childen
            }
        </Tab>
    )
}
    */
export default class TabComponent extends Component<TabProps>{
    constructor(props:TabProps) {
            super(props);
    }
    render(){
        return (
        <Tab eventKey={this.props.eventKey} title={this.props.title} disabled={this.props.disabled}>
        {this.props.childen}
        </Tab>
    );
    }
}
/*
const TabComponent: React.FC<TabProps> = ({eventKey,title,childen,disabled}:TabProps) => {
    return (
        <Tab eventKey={eventKey} title={title} disabled={disabled}>
        {childen}
        </Tab>
    );
  };
  export default TabComponent;
  */