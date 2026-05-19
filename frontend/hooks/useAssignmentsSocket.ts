"use client";

import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useEffect, useRef, useState } from "react";
import { API_BASE_URL } from "@/services/api";

export function useAssignmentsSocket(onUpdate: () => void) {
  const callbackRef = useRef(onUpdate);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    callbackRef.current = onUpdate;
  }, [onUpdate]);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}/ws`),
      reconnectDelay: 3000,
      onConnect: () => {
        setConnected(true);
        client.subscribe("/topic/assignments", () => callbackRef.current());
      },
      onDisconnect: () => setConnected(false),
      onWebSocketClose: () => setConnected(false)
    });

    client.activate();
    return () => {
      void client.deactivate();
    };
  }, []);

  return { connected };
}
