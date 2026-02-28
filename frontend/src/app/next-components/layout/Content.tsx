'use client'
import { Component, ReactNode, RefObject } from "react";
import { Container } from "react-bootstrap";
import { connect } from "react-redux";
export type ObjectRecord=Record<string,any>|null;
export type ObjectArray=ObjectRecord[];
interface Props{
    children?:ReactNode;
    login:boolean;
    user:Record<string,any>;
}
class Content extends Component<Props>{
    public login=this.props.login;
    public user=this.props.user;
    
    addInputRefComponent(array:RefObject<Array<any>>,component:Component|null){
        const exists=array.current.find((ele:any)=>component==ele)||null;
        if(exists==null)array.current.push(component);
        return array;
    }

    forceUpdateRefComponents(array:RefObject<Array<any>>){
        array.current.map((ele:Component)=>{
            const comp=ele;
            comp?.forceUpdate();
        });
    }

    render(){
        return(
            <Container>
            {
                this.props.children
            }
            </Container>
        )
    }
}
const mapStateToProps=(state:Record<string,any>)=>({
    user:state.login,
    login:state.login.login
});
export default connect(mapStateToProps)(Content);
