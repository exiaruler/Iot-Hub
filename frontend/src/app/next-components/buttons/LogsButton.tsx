'use client';

import { useRouter } from "next/navigation";
import { RegularButton } from "./RegularButton";

interface Props {
    entity: string;
    id: string;
}

export default function LogsButton(props: Props) {
    const router = useRouter();
    const handleClick = () => {
        router.push(`/log/${props.entity.toLowerCase()}/${props.id}`);
    };

    return (
        <RegularButton caption={"Logs"} size={undefined} type={undefined} onClick={handleClick} />
    );
}
