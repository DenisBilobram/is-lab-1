import { Injectable, OnDestroy } from '@angular/core';
import { Client, IMessage, Message } from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';
import { API_URLS } from '../app.config';

@Injectable({
  providedIn: 'root'
})
export class WebScoketService {

  private client: Client;
  private subjects: { [topic: string]: Subject<any> } = {};

  constructor() {
    this.client = new Client({
      brokerURL: API_URLS.WEBSOCKET,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = () => {
      console.log('Connected to WebSocket');
      for (const topic in this.subjects) {
        if (this.subjects.hasOwnProperty(topic)) {
          this.subscribeToTopic(topic);
        }
      }
    };

    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.activate();
  }

  private subscribeToTopic(topic: string) {
    this.client.subscribe(`/topic/${topic}`, (message: IMessage) => {
      this.subjects[topic].next(JSON.parse(message.body));
    });
  }

  subscribe(topic: string): Observable<any> {
    if (!this.subjects[topic]) {
      this.subjects[topic] = new Subject<any>();
      if (this.client.connected) {
        this.subscribeToTopic(topic);
      }
    }

    return this.subjects[topic].asObservable();
  }


  disconnect() {
    if (this.client.active) {
      this.client.deactivate();
      console.log('Disconnected from WebSocket');
    }
  }

}
