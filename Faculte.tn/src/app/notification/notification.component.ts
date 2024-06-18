import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { WebsocketService } from '../Services/websocket.service';
import { UserService } from '../Services/user.service';
import { NotificationService } from '../Services/notification.service';
import { FriendshipService } from '../Services/friendship.service';


@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {
  showNotificationDropdown: boolean = false;
  notifications: any[] = [];
  loggedInUserEmail!: string;
  loggedInUserId!:number;
  private notificationSubscription!: Subscription;

  constructor(private websocketService: WebsocketService,
              private notificationService: NotificationService,
              private friendshipService:FriendshipService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.fetchLoggedInUserId();
    this.fetchLoggedInUserDetails();
  }

  ngOnDestroy(): void {
    // Unsubscribe from the notification subscription to prevent memory leaks
    this.notificationSubscription.unsubscribe();
  }

  toggleNotificationDropdown() {
    this.showNotificationDropdown = !this.showNotificationDropdown;
  }

  fetchLoggedInUserId(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.loggedInUserId = userId;
        this.fetchLoggedInUserDetails();
        this.fetchNotifications();
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  fetchLoggedInUserDetails(): void {
    this.userService.getUserDetails(this.loggedInUserId).subscribe(
      userDetails => {
        // Assuming userDetails contains email information
        this.loggedInUserEmail = userDetails.email;
        this.connectToWebSocket();
      },
      error => {
      }
    );
  }

  connectToWebSocket(): void {
    this.websocketService.connect().subscribe(() => {
      console.log('Connected to WebSocket');
      // Subscribe to receive notifications after connecting
      this.notificationSubscription = this.websocketService.subscribeToNotifications(this.loggedInUserId.toString()).subscribe((notification: any) => {
        // Received a new notification, add it to the notifications array
        this.notifications.unshift(notification); // Add notification to the beginning of the array
        console.log('Received notification:', notification); // Log the received notification
      });
    });  
  }

  fetchNotifications(): void {
    this.notificationService.getAllNotificationsByUserId(this.loggedInUserId).subscribe(
      notifications => {
        this.notifications = notifications.filter(notification => notification.type !== 'MESSAGE');
      },
      error => {
        console.error('Error fetching notifications:', error);
      }
    );
  }

  isFriendRequestNotification(notification: any): boolean {
    return notification.type === 'FRIEND_REQUEST';
  }

  

  acceptFriendRequest(notification: any): void {
    const notificationId = notification.id; // Assuming id is available in the notification
    const userId = notification.senderId; // Assuming senderId is available in the notification
    
    
    this.friendshipService.acceptFriendRequest(userId, this.loggedInUserId).subscribe(
      response => {
        // If the request is successfully accepted, update the notification message and hide buttons
        const index = this.notifications.findIndex(notif => notif.id === notificationId);
        if (index !== -1) {
          this.notifications[index].message = "You are now friends  ";
          this.notifications[index].showButtons = false;
          // Update the original notifications array with the modified notification
          this.notificationService.updateNotification(this.notifications[index]).subscribe(
            updatedNotification => {
              console.log('Notification updated successfully:', updatedNotification);
            },
            error => {
              console.error('Error updating notification:', error);
            }
          );
        }
        console.log('Friend request accepted successfully:', response);
      },
      error => {
        console.error('Error accepting friend request:', error);
      }
    );
  }
  
  declineFriendRequest(notification: any): void {
    const notificationId = notification.id; // Assuming id is available in the notification
    const userId = notification.senderId; // Assuming senderId is available in the notification
    this.friendshipService.rejectFriendRequest(userId, this.loggedInUserId).subscribe(
      response => {
        // If the request is successfully declined, update the notification message and hide buttons
        const index = this.notifications.findIndex(notif => notif.id === notificationId);
        if (index !== -1) {
          this.notifications[index].message = "Friendship declined";
          this.notifications[index].showButtons = false;
          // Update the original notifications array with the modified notification
          this.notificationService.updateNotification(this.notifications[index]).subscribe(
            updatedNotification => {
              console.log('Notification updated successfully:', updatedNotification);
            },
            error => {
              console.error('Error updating notification:', error);
            }
          );
        }
        console.log('Friend request declined successfully:', response);
      },
      error => {
        console.error('Error declining friend request:', error);
      }
    );
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
