import {LikesInfo} from "./like-details.type";
import {AuthorDto} from "./author.type";

export class Article {
  public author: AuthorDto;
  public timestamp: Date;
  public text: string;
  public id: string;
  public elapsed: string;
  public createDate: string;
  public likes: LikesInfo;
  public numOfComments: number;

  constructor(
    author: AuthorDto,
    timestamp: Date,
    text: string,
    id: string,
    elapsed: string,
    createDate: string,
    likes: LikesInfo,
    numOfComments: number
  ) {
    this.author = author;
    this.timestamp = timestamp;
    this.text = text;
    this.id = id;
    this.elapsed = elapsed;
    this.createDate = createDate;
    this.likes = likes;
    this.numOfComments = numOfComments;
  }
}
