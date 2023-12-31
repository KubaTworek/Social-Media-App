export class AuthorDto {
  public id: string;
  public username: string;
  public firstName: string;
  public lastName: string;
  public isFollowed: boolean;

  constructor(
    id: string,
    username: string,
    firstName: string,
    lastName: string,
    isFollowed: boolean
  ) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isFollowed = isFollowed;
  }
}
