import {LikesInfo} from "./likes-info.type";
import {Author} from "./author.type";

export class Article {
  public author: Author;
  public timestamp: Date;
  public text: string;
  public id: string;
  public elapsed: string;
  public createDate: string;
  public likes: LikesInfo;
  public numOfLikes: number;

  constructor(
    author: Author,
    timestamp: Date,
    text: string,
    id: string,
    elapsed: string,
    createDate: string,
    likes: LikesInfo,
    numOfLikes: number
  ) {
    this.author = author;
    this.timestamp = timestamp;
    this.text = text;
    this.id = id;
    this.elapsed = elapsed;
    this.createDate = createDate;
    this.likes = likes;
    this.numOfLikes = numOfLikes;
  }
}
