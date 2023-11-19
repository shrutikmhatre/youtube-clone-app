import {Component, inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatChipEditedEvent, MatChipInputEvent,MatChipsModule} from "@angular/material/chips";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {ActivatedRoute} from "@angular/router";
import {VideoService} from "../video.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {VideoDTO} from "../VideoDTO";

@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrls: ['./save-video-details.component.css'],
})
export class SaveVideoDetailsComponent implements OnInit{
  saveVideoDetailsForm :  FormGroup;
  title:FormControl=new FormControl('');
  description:FormControl=new FormControl('');
  videoStatus:FormControl=new FormControl('');
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: string[] = [];
  announcer = inject(LiveAnnouncer);
  selectedFile! :any
  selectedFileName: any;
  videoId = '';
  fileSelected!:boolean;
  videoUrl :string = '' ;
  thumbnailUrl: string ='';
  constructor(private activatedRouter:ActivatedRoute,  private videoService:VideoService,
              private _snackBar: MatSnackBar) {
    this.videoId = this.activatedRouter.snapshot.params['videoId'];
    this.videoService.getVideoDetails(this.videoId).subscribe(data => {
     this.videoUrl = data.videoUrl;
     console.log("parent video url ",this.videoUrl);


    });
    this.saveVideoDetailsForm=new FormGroup({
      title:this.title,
      description:this.description,
      videoStatus:this.videoStatus
    })
  }
  ngOnInit(): void {

  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(tag: string): void {
    const index = this.tags.indexOf(tag);

    if (index >= 0) {
      this.tags.splice(index, 1);

      this.announcer.announce(`Removed ${tag}`);
    }
  }
  edit(tag: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    // Remove fruit if it no longer has a name
    if (!value) {
      this.remove(tag);
      return;
    }

    // Edit existing fruit
    const index = this.tags.indexOf(tag);
    if (index >= 0) {
      this.tags[index] = value;
    }
  }




  onUpload() {
    this.videoService.uploadThumbnail(this.selectedFile , this.videoId).subscribe(
      (data :string)  =>{
        console.log("response data  of upload thumbnail" +data);
        this.thumbnailUrl = data;
        this._snackBar.open("Thumbnail upload successful", "OK");
      });
  }

  saveVideo() {
      //call video service to make http call to backend
    const videoMetadata : VideoDTO={
      "id" : this.videoId,
      "title" :this.saveVideoDetailsForm.get('title')?.value,
      "description" : this.saveVideoDetailsForm.get('description')?.value,
      "tags" : this.tags,
      "videoStatus" : this.saveVideoDetailsForm.get('videoStatus')?.value,
      "videoUrl" :this.videoUrl,
      "thumbnailUrl"  : this.thumbnailUrl,
      "likeCount" :0,
      "dislikeCount":0,
      "viewCount":0
    }
    this.videoService.saveVideo(videoMetadata).subscribe(
      data =>{
        this._snackBar.open("Video metadata updated successfully","OK");
      }
    )

  }

  onFileSelected($event: Event) {
      // @ts-ignore
      this.selectedFile = event.target.files[0];
      this.selectedFileName=this.selectedFile.name;
      this.fileSelected = true;
  }
}
