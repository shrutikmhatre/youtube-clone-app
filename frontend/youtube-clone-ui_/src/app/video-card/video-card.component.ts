import {Component, Input, OnInit} from '@angular/core';
import {VideoDTO} from "../VideoDTO";
import {VideoService} from "../video.service";

@Component({
  selector: 'app-video-card',
  templateUrl: './video-card.component.html',
  styleUrls: ['./video-card.component.css']
})
export class VideoCardComponent  implements OnInit{

  @Input() video!: VideoDTO;

  constructor() {
  }

  ngOnInit() {
  }


}
