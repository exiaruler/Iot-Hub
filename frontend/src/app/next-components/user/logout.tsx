import { useDispatch } from "react-redux";
import { clearUser } from "@/redux/slice/loginSlice";
import { NextUIBase } from "@/NextUIBase";
import Link from 'next/link';
import { Nav } from "react-bootstrap";

export default function Logout(){
  const base=new NextUIBase();
  const dispatch=useDispatch();

  const logOut= async () => {
    try{
        const log=await base.userApi.logout();
        if(log){
            base.util.removeLogCookie();
            dispatch(clearUser());
            window.location.href = "/";
        }
    }catch(err){
        throw err;
    }
  } 
  return(
    <Nav.Link key={0}><Link onClick={logOut} href={'/'}>{'Logout'}</Link></Nav.Link>
  );
}
