import { Component, OnInit, Input, ChangeDetectorRef, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WebsocketService } from '../Services/websocket.service';
import { UserService } from '../Services/user.service';
import { ChatmessageService } from '../Services/chatmessage.service';
import { ChatroomService } from '../Services/chatroom.service'; 

@Component({
  selector: 'app-chat-dialog',
  templateUrl: './chat-dialog.component.html',
  styleUrls: ['./chat-dialog.component.css']
})
export class ChatDialogComponent implements OnInit {
  message: string = '';
  loggedInUserId!: number;
  showChatContainer: boolean = false;
  chatMessages: any[] = [];
  showMessageOptions: boolean = false;

  @Input() friendId: number = 0;
  @Input() friendFirstName: string = '';
  @Input() friendLastName: string = '';
  @ViewChild('chatMessagesContainer') private chatMessagesContainer!: ElementRef;

  constructor(
    private websocketService: WebsocketService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private chatmessageService: ChatmessageService,
    private chatroomService: ChatroomService,
    private cdr: ChangeDetectorRef
    
  ) {
    this.friendId = this.activatedRoute.snapshot.params['friendId'];
  }

  toggleChatContainer() {
    this.showChatContainer = !this.showChatContainer;
  }

  ngOnInit() {
    this.fetchLoggedInUserId();
    this.websocketService.connect().subscribe(() => {
      console.log('WebSocket connected');
      this.websocketService.subscribeToMessages().subscribe((response: any) => {
        console.log('Received message:', response);
        const message = response.body; // Extract the message from the body property
        if (message.senderId === this.friendId) { // Check if message is from the friend
          this.chatMessages.push(message); // Only push message if it's from the friend
          this.scrollToBottom(); // Scroll to bottom after receiving the message
        }
      });

      // Subscribe to edited messages
      this.websocketService.subscribeToEditedMessages().subscribe((editedMessage: any) => {
        console.log('Received edited message:', editedMessage);
        // Find the edited message in the chatMessages array and update it
        const index = this.chatMessages.findIndex(msg => msg.id === editedMessage.id);
        if (index !== -1) {
          this.chatMessages[index].content = editedMessage.content;
          // Optionally, you can add some UI indication that the message has been edited
        }
      });
    });
  }
  
  
  sendMessage(): void {
    if (this.message.trim() !== '') {
      const senderId = this.loggedInUserId;
      const recipientId = this.friendId;
      const messageObject = {
        senderId: senderId,
        content: this.message
      };
      this.websocketService.sendMessage(this.message, senderId, recipientId);
      this.chatMessages.push(messageObject);
      this.message = '';
      this.scrollToBottom();
    }
  }
  
  

  fetchLoggedInUserId(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.loggedInUserId = userId;
        console.log('Logged-in user ID:', this.loggedInUserId);
        this.fetchChatRoom();
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  fetchChatRoom(): void {
    this.chatroomService.getChatRoomByUserIds(this.loggedInUserId, this.friendId).subscribe(
      chatRoom => {
        console.log('Chat room:', chatRoom);
        if (chatRoom) {
          const chatRoomId = chatRoom.id;
          this.chatmessageService.getChatMessages(chatRoomId).subscribe(
            messages => {
              console.log('Fetched chat messages:', messages);
              this.chatMessages = messages;
              this.scrollToBottom();
            },
            error => {
              console.error('Error fetching chat messages:', error);
            }
          );
        }
      },
      error => {
        console.error('Error fetching chat room:', error);
      }
    );
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }
  
  private scrollToBottom(): void {
    try {
      this.chatMessagesContainer.nativeElement.scrollTop = this.chatMessagesContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }


  showOptionsPanel() {
    this.showMessageOptions = true;
  }
  
  hideOptionsPanel(msg:any) {
    this.showMessageOptions = false;
    msg.showEditMenu = false;

  }
  
  toggleEditMenu(msg: any): void {
    msg.showEditMenu = !msg.showEditMenu;
  }

  
  editMessage(msg: any) {
    msg.isEditing = true;
    msg.editContent = msg.content; 
  }

  saveEditedMessage(msg: any): void {
    this.chatmessageService.editMessage(msg.id, msg.editContent).subscribe(
      updatedMessage => {
        msg.content = updatedMessage.content;
        msg.isEditing = false;
        msg.editContent = '';
      },
      error => {
        console.error('Error editing message:', error);
      }
    );
  }
  

  cancelEditing(msg: any) {
    msg.isEditing = false;
    msg.editContent = ''; // Clear the edit content
  }

  

}
