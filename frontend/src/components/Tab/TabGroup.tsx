'use client'
import React, { Component } from "react";
import { forwardRef, ReactElement, ReactNode, useImperativeHandle, useState } from "react";
import { Tabs } from "react-bootstrap";
import TabProps from './TabProps';
import TabComponent from "./TabComponent";
type Props={
    id?:string;
    defaultActiveKey:any;
    children?:React.ReactElement<TabComponent,any>[];
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
/*
 const TabGroup=forwardRef(function TabGroup(props:Props,ref){
    const [activeTab, setActiveTab] = useState(props.defaultActiveKey);

    const handleTabSwitch = (tabKey:any) => {
        setActiveTab(tabKey);
      };
    const onSelectTab=(tabKey:any)=>{
        setActiveTab(tabKey);
        if(props.onSelect){
            props.onSelect(tabKey);
        }
    }
    useImperativeHandle(ref,()=>{
        return {
            handleTabSwitch
        }
    },[]);
  
    return (
        <div>
        <Tabs
        className="mb-4"
        activeKey={activeTab}
        onSelect={(tabKey:any)=>onSelectTab(tabKey)}
        defaultActiveKey={props.defaultActiveKey}
        >
        {
            props.children
        }
        </Tabs>
        </div>
    );
});
export default TabGroup;
*/