import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient: Stomp.Client;
  private messageSubject: Subject<any> = new Subject<any>();
  private notificationSubject: Subject<any> = new Subject<any>();


  constructor() { 
    this.stompClient = Stomp.over(new SockJS('https://facultetnbackend.onrender.com/ws'));
  }

  connect(): Observable<void> {
    return new Observable<void>(observer => {
      const socket = new SockJS('https://facultetnbackend.onrender.com/ws');
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, () => {
        console.log('Connected to WebSocket');
        observer.next(); // Notify observers about successful connection
      }, error => {
        console.error('Error establishing WebSocket connection:', error);
        observer.error(error); // Notify observers about connection error
      });
    });
  }

  sendMessage(message: string, senderId: number, recipientId: number): void {
    this.stompClient.send('/app/messages/send', {}, JSON.stringify({ senderId, recipientId, content: message }));
  }
  

  receiveMessages(): Observable<any> {
    return this.messageSubject.asObservable();
  }
  

  subscribeToMessages(): Observable<any> {
    return new Observable<any>(observer => {
      const subscription = this.stompClient.subscribe('/topic/messages', (message: any) => {
        const parsedMessage = JSON.parse(message.body);
        observer.next(parsedMessage); // Emit the parsed message body
      });
      // Return the teardown logic to unsubscribe when the observable is unsubscribed
      return () => subscription.unsubscribe();
    });
  }
  

  sendNotification(notification: any): void {
    this.stompClient.send('/app/notifications/send', {}, JSON.stringify(notification));
  }
  

  receiveNotifications(): Observable<any> {
    return this.notificationSubject.asObservable();
  }

  subscribeToNotifications(userId: string): Observable<any> {
    return new Observable<any>(observer => {
      const subscription = this.stompClient.subscribe(`/user/${userId}/queue/notifications`, (notification: any) => {
        const parsedNotification = JSON.parse(notification.body);
        console.log('Received notification:', parsedNotification); // Log the received notification
        observer.next(parsedNotification); // Emit the parsed notification body
      });
      // Return the teardown logic to unsubscribe when the observable is unsubscribed
      return () => subscription.unsubscribe();
    });
  }
  
  
  
  editMessage(chatMessage: any): void {
    this.stompClient.send('/app/editMessage', {}, JSON.stringify(chatMessage));
  }

  subscribeToEditedMessages(): Observable<any> {
    return new Observable<any>(observer => {
      const subscription = this.stompClient.subscribe('/topic/editMessage', (message: any) => {
        const parsedMessage = JSON.parse(message.body);
        observer.next(parsedMessage); // Emit the parsed message body
      });
      // Return the teardown logic to unsubscribe when the observable is unsubscribed
      return () => subscription.unsubscribe();
    });
  }


  
}
