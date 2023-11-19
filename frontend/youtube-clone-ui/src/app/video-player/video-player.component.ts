import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {VgApiService} from "@videogular/ngx-videogular/core";

@Component({
  selector: 'app-video-player',
  templateUrl: './video-player.component.html',
  styleUrls: ['./video-player.component.css']
})
export class VideoPlayerComponent implements OnInit,OnChanges{

  api!: VgApiService;
 @Input() videoUrl :any ;


 constructor() {
     console.log('video url::',this.videoUrl);
  }

  ngOnInit() {

  }
    ngOnChanges(changes: SimpleChanges) {
        console.log('video url::',this.videoUrl);
    }

    onPlayerReady(api: VgApiService) {
        this.api = api;

        this.api.getDefaultMedia().subscriptions.ended.subscribe(
            () => {

                // Set the video to the beginning
                this.api.getDefaultMedia().currentTime = 0;
            }
        );
    }
}
