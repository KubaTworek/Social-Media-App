import {LikesInfo} from "./likes-info.type";
import {AuthorDto} from "./author.type";

export class Article {
  public author: AuthorDto;
  public timestamp: Date;
  public text: string;
  public id: string;
  public elapsed: string;
  public createDate: string;
  public likes: LikesInfo;
  public numOfLikes: number;
  public numOfComments: number;

  constructor(
    author: AuthorDto,
    timestamp: Date,
    text: string,
    id: string,
    elapsed: string,
    createDate: string,
    likes: LikesInfo,
    numOfLikes: number,
    numOfComments: number
  ) {
    this.author = author;
    this.timestamp = timestamp;
    this.text = text;
    this.id = id;
    this.elapsed = elapsed;
    this.createDate = createDate;
    this.likes = likes;
    this.numOfLikes = numOfLikes;
    this.numOfComments = numOfComments;
  }
}
