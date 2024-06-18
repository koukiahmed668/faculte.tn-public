import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../Services/user.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'https://facultetnbackend.onrender.com/api/post';
  private bookmark = 'https://facultetnbackend.onrender.com/api/bookmarks';

  constructor(private http: HttpClient, private userService: UserService ) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      // Handle the case where token is not available
      return new HttpHeaders();
    }
  }

  createPost(postContent: string, files: File[], selectedSubjectId: number | null): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.userService.getUserId().subscribe(userId => {
        const formData = new FormData();
        formData.append('content', postContent);
        if (files && files.length > 0) {
          // Loop through each file and append it to the formData
          for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i], files[i].name); // Ensure the correct key 'files' is used here
          }
        }
        if (selectedSubjectId !== null) {
          formData.append('subjectId', selectedSubjectId.toString());
        }
        const headers = this.getHeaders();
        this.http.post<any>(`${this.apiUrl}/create?userId=${userId}`, formData, { headers })
          .subscribe(response => {
            console.log('Post created:', response);
            resolve();
          }, error => {
            console.error('Error creating post:', error);
            reject(error);
          });
      }, error => {
        console.error('Error fetching user ID:', error);
        reject(error);
      });
    });
  }
  

  savePost(userId: number, postId: number): Observable<string> {
    const url = `${this.bookmark}/add`;
    const params = {
      userId: userId.toString(),
      postId: postId.toString()
    };
    const headers = this.getHeaders();
    return this.http.post<string>(url, null, { params, headers });
  }

  getBookmarkedPosts(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.bookmark}/${userId}`, { headers });
  }

  deletePost(postId: number, loggedInUserId: number): Observable<void> {
    const url = `${this.apiUrl}/delete/${postId}`;
    const headers = this.getHeaders();
    // Pass the loggedInUserId as a request parameter
    return this.http.delete<void>(url, { params: { loggedInUserId: loggedInUserId.toString() }, headers });
  }

  updatePost(postId: number, userId: number, content: string, files: File[] | null): Observable<any> {
    const formData = new FormData();
    formData.append('userId', userId.toString());
    formData.append('content', content);
    if (files) {
      for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
      }
    }
    const headers = this.getHeaders();
    return this.http.put<any>(`${this.apiUrl}/update/${postId}`, formData, { headers });
  }

  getAllPosts(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/allposts`, { headers });
  }

  sharePost(postId: number, userId: number, additionalContent?: string): Observable<string> {
    const url = `${this.apiUrl}/share`;
    const params = {
      postId: postId.toString(),
      userId: userId.toString(),
      additionalContent: additionalContent ? additionalContent : ''
    };
    const headers = this.getHeaders();
    return this.http.post<string>(url, null, { params, headers });
  }

  getSharedPosts(userId: number): Observable<any[]> {
    const url = `${this.apiUrl}/shared`;
    // Pass userId as a query parameter
    const headers = this.getHeaders();
    return this.http.get<any[]>(url, { params: { userId: userId.toString() }, headers });
  }

  getPostsByUserId(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/postsbyuser?userId=${userId}`, { headers });
  }

  getFileData(fileId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get(`https://facultetnbackend.onrender.com/api/files/files/${fileId}/blob`, { headers, responseType: 'blob' });
}


  getPostsBySubjectId(subjectId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/posts/${subjectId}`, { headers });
  }

  getAllPostsBySpecialty(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/getbyspecialty`, { headers });
  }

  likePost(postId: number, userId: number): Observable<number> {
    const url = `${this.apiUrl}/${postId}/like?userId=${userId}`;
    const headers = this.getHeaders();
    return this.http.post<number>(url, null, { headers });
  }

  addCommentToPost(postId: number, userId: number, content: string): Observable<Comment> {
    const url = `${this.apiUrl}/${postId}/comments`;
    const params = {
      userId: userId.toString(),
      content: content
    };
    const headers = this.getHeaders();
    return this.http.post<Comment>(url, null, { params, headers });
  }

  removeCommentFromPost(postId: number, commentId: number, userId: number): Observable<void> {
    const url = `${this.apiUrl}/${postId}/comments/${commentId}?userId=${userId}`;
    const headers = this.getHeaders();
    return this.http.delete<void>(url, { headers });
  }
  
  updateCommentInPost(postId: number, commentId: number, userId: number, newContent: string): Observable<any> {
    const url = `${this.apiUrl}/${postId}/comments/${commentId}`;
    const params = {
      userId: userId.toString(),
      newContent: newContent
    };
    const headers = this.getHeaders();
    return this.http.put<any>(url, null, { params, headers });
  }

  getCommentsByPostId(postId: number): Observable<Comment[]> {
    const url = `${this.apiUrl}/${postId}/comments`;
    const headers = this.getHeaders();
    return this.http.get<Comment[]>(url, { headers });
  }
}
