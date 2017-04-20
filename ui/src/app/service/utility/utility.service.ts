import { Injectable } from '@angular/core';
import { Artist } from '../../model/Artist';
import { Album } from '../../model/Album';
import { Song } from '../../model/Song';
import { SongInfo } from '../../model/SongInfo';

@Injectable()
export class UtilityService {

  constructor() { }

  getShuffledSongId(currSongs: Array<Song>, selSongId: number): number {
      let currSongIndex = 0;
      for (let i = 0; i < currSongs.length; i++) {
        if (currSongs[i].id == selSongId) { currSongIndex = i; }
      }
      let randomInt = currSongIndex;  //Initialize this way to enter while loop.
      while (randomInt == currSongIndex) {
        randomInt = this.getRandomInt(0, currSongs.length - 1);
      }
      return currSongs[randomInt].id;
    }

  getRandomInt(min, max) {
    return Math.round(Math.random() * (max - min) + min);
  }

}