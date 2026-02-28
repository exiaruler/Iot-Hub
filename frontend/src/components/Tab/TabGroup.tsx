'use client'
import React, { Component } from "react";
import { forwardRef, ReactElement, ReactNode, useImperativeHandle, useState } from "react";
import { Tabs } from "react-bootstrap";
import TabProps from './TabProps';
import TabComponent from "./TabComponent";
type Props={
    id?:string;
    defaultActiveKey:any;
    children?:ReactNode;
    others?:any;
    onSelect?:any;
}
interface State{
    activeTab:string|any;
}
export default class TabGroup extends Component<Props,State>{
    constructor(props:Props) {
            super(props);
            this.state = {
                activeTab:this.props.defaultActiveKey
            };
    }
    handleTabSwitch(tabKey:any){
        this.setState({...this.state,activeTab:tabKey});
    }
    onSelectTab=(tabKey:any)=>{
        this.setState({...this.state,activeTab:tabKey});
        if(this.props.onSelect){
            this.props.onSelect(tabKey);
        }
    }
    render(){
        return (
        <div>
        <Tabs
        className="mb-4"
        activeKey={this.state.activeTab}
        onSelect={(tabKey:any)=>this.onSelectTab(tabKey)}
        defaultActiveKey={this.props.defaultActiveKey}
        >
        {
            this.props.children
        }
        </Tabs>
        </div>
    );
    }

}
