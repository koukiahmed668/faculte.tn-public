import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { WebsocketService } from '../Services/websocket.service';
import { UserService } from '../Services/user.service';
import { NotificationService } from '../Services/notification.service';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent implements OnInit, OnDestroy {
  chatRooms: any[] = [];
  loggedInUserId!: number;
  notifications: any[] = []; // Change notifications array type to any

  private notificationSubscription!: Subscription;

  constructor(
    private websocketService: WebsocketService,
    private userService: UserService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.fetchLoggedInUserId();
    this.connectToWebSocket();
  }

  ngOnDestroy(): void {
    this.notificationSubscription.unsubscribe();
  }

  fetchLoggedInUserId(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.loggedInUserId = userId;
        console.log('Logged-in user ID:', this.loggedInUserId);
        this.fetchNotifications();
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  fetchNotifications(): void {
    this.notificationService.getAllNotificationsByUserId(this.loggedInUserId).subscribe(
      notifications => {
        this.notifications = notifications;
      },
      error => {
        console.error('Error fetching notifications:', error);
      }
    );
  }

  connectToWebSocket(): void {
    this.websocketService.connect().subscribe(() => {
      console.log('Connected to WebSocket');
      // Subscribe to receive notifications after connecting
      this.notificationSubscription = this.websocketService.subscribeToNotifications(this.loggedInUserId.toString()).subscribe((notification: any) => {
        // Received a new notification, add it to the notifications array
        if (notification.type === 'MESSAGE') {
          this.notifications.unshift(notification); // Add notification to the beginning of the array
          console.log('Received message notification:', notification); // Log the received notification
        }
      });
    });
  }


  deleteNotification(notificationId: number): void {
    this.notificationService.deleteNotification(notificationId).subscribe(
      () => {
        // If the notification is successfully deleted, remove it from the notifications array
        const index = this.notifications.findIndex(notif => notif.id === notificationId);
        if (index !== -1) {
          this.notifications.splice(index, 1); // Remove the notification from the array
          console.log('Notification deleted successfully');
        }
      },
      error => {
        console.error('Error deleting notification:', error);
      }
    );
  }

  
}
