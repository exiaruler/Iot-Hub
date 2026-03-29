import { Card,CardBody,CardTitle,CardText, Col, Row, Button } from "react-bootstrap";
import Client from "./client";
import { RegularButton } from "../next-components/buttons/RegularButton";
import { NextBase } from "@/NextBase";

async function getBoards() {
  let dataResp={
    boards:[],
    hardwares:[]
  };
  const base=new NextBase();
  // fetch user boards
  let boards=await base.fetchClientGet('/board/getboards');
  dataResp.boards=boards;
  // fetch hardware boards
  dataResp.hardwares=await base.fetchClientGet('/hardware/get-hardwares');
  return dataResp;
}
export default async function Page(){
    const data=await getBoards();
    return(
    <Client boards={data.boards} hardwares={data.hardwares}/>
    
    );
}