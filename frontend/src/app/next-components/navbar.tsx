'use client'
import React, { useEffect, useState } from 'react';
import { NextUIBase } from '@/NextUIBase';
import Link from 'next/link';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { setPages } from '@/redux/slice/pageSlice';
import { useDispatch, useSelector } from 'react-redux';
import { setApp } from '@/redux/slice/appSlice';
import { getLoginState, setUser } from '@/redux/slice/loginSlice';
import Logout from './user/logout';
  
export default function NavBar(){
    const dispatch = useDispatch();
    const uiBase=new NextUIBase();
    const [login,setLogin]=useState(false);
    const loginState=useSelector(getLoginState);
    const [pageArr,setPageArr]:any=useState([]);
    //const navigation = new Navigation();
    let protectedRoutes:any=[
        
    ];
    let formsUser:any=[
        
    ];
   
    const routes=uiBase.getPagesSection('navbar');

    const checkLogin= async()=>{
      const userDet=await uiBase.userApi.userDetailsJwt();
      if(userDet!=null&&uiBase.util.checkLogCookie()){
        dispatch(setUser(Object(userDet)));
      }else{
        uiBase.util.removeLogCookie();
        localStorage.removeItem('login');
      }
      setLogin(loginState);
    }
    
    const loadApp=async()=>{
      const getAppSes=await uiBase.util.sessionGet('app');
      const sessionPages=uiBase.getPagesSession();
      if(sessionPages.length>0){
        dispatch(setPages(sessionPages));
      }
      if(getAppSes!=null){
        dispatch(setApp(getAppSes));
      }else
      {
        const getApp=await uiBase.util.getApp();
        if(getApp!=null){
          dispatch(setApp(getApp));
          const pages=getApp.pages;
          // set pages
          dispatch(setPages(pages));
          uiBase.util.pageSession(pages);
          setPageArr(pages);
        }
      }
  }
    const renderUserDrop=()=>{
      return <NavDropdown title="Tools" id="basic-nav-dropdown" >
          {
            // forms
            formsUser.map((link:any)=>(
              <Nav.Link><Link href={link.url}>{link.name}</Link></Nav.Link> 
            ))
          }
          {
            // protected routes
            protectedRoutes.map((link:any)=>(
              <Nav.Link><Link href={link.url}>{link.name}</Link></Nav.Link> 
            ))
          }
         
          </NavDropdown>
    }
    const renderLink=(route:Record<string,any>,key:number)=>{
      if(route.url!='/login'){
        const ele=<Nav.Link key={key}><Link key={key} href={route.url}>{route.name}</Link></Nav.Link>
        return ele;
      }else{ 
        const ele=<Nav.Link key={key}><Link key={key} href={route.url}>{route.name}</Link></Nav.Link>
        if(!loginState) return ele;
      
      }
    }
    
    useEffect(() => {
      loadApp();
      checkLogin();
   },[]);
   
    return (
    <div id="NavBar">
    <nav>
    <Navbar expand="lg" className="bg-body-tertiary" data-bs-theme="light">
      <Container>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            {
                routes.map((route:Record<string,any>,key:number)=>(
                   renderLink(route,key)
                ))
            }
            {loginState?
            <Logout/>    
            :null}
          {login?
            renderUserDrop()
          :null}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
    </nav>
    </div>
    )
}