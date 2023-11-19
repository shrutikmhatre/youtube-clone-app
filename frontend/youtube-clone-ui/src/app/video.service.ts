import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {Observable} from "rxjs";
import {UploadVideoResponse} from "./upload-video/UploadVideoResponse";
import {VideoDTO} from "./VideoDTO";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private  httClient:HttpClient) { }

  uploadVideo(fileEntry:File) : Observable<UploadVideoResponse>{
    //http post call to upload vide
    const formData =  new FormData();
    formData .append('file',fileEntry,fileEntry.name );
    return this.httClient.post<UploadVideoResponse>("http://localhost:8080/api/videos/upload",formData);
  }

  uploadThumbnail(fileEntry:File,videoId:string) : Observable<string>{
    //http post call to upload vide
    const formData =  new FormData();
    formData .append('file',fileEntry,fileEntry.name );
    formData .append('videoId',videoId);
    return this.httClient.post("http://localhost:8080/api/videos/thumbnail",formData,{
      responseType : "text"
    } );
  }

  getVideoDetails(videoId:string):Observable<VideoDTO>{
    return this.httClient.get<VideoDTO>("http://localhost:8080/api/videos/getVideoDetails/" + videoId);

  }

  saveVideo(videoMetadata: VideoDTO) : Observable<VideoDTO> {
    return this.httClient.put<VideoDTO>("http://localhost:8080/api/videos/edit",videoMetadata);
  }

  getAllVideos():Observable<Array<VideoDTO>>{
    return this.httClient.get<Array<VideoDTO>>("http://localhost:8080/api/videos/getAllVideos");
  }

  likeVideo(videoId: string): Observable<VideoDTO> {
      return this.httClient.post<VideoDTO>(`http://localhost:8080/api/videos/${videoId}/like`,null);
  }

  dislikeVideo(videoId: string): Observable<VideoDTO> {
    return this.httClient.post<VideoDTO>(`http://localhost:8080/api/videos/${videoId}/dislike`,null);
  }
}
