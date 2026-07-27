'use client'
import { Tab, TabsProps } from "react-bootstrap";
import TabProps from './TabProps';
import { Component } from "react";
export default class TabComponent extends Component<TabProps>{
    
    render(){
        return (
        <Tab {...this.props.others} hidden={this.props.hidden} eventKey={this.props.eventKey} title={this.props.title} disabled={this.props.disabled}>
        {this.props.children}
        </Tab>
    );
    }
}
