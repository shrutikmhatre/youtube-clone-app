import {Component, OnInit} from '@angular/core';
import {VideoService} from "../video.service";
import {VideoDTO} from "../VideoDTO";

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit{
  featuredVideos:Array<VideoDTO>=[];
  constructor(private videoService:VideoService) {
  }

  ngOnInit() {
    this.videoService.getAllVideos().subscribe(
      (response) =>{
        this.featuredVideos=response;
      }
    );


  }
}
