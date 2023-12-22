export class Notification {
  public id: string;
  public name: string;
  public message: string;
  public content: string;

  constructor(id: string, name: string, message: string, content: string) {
    this.id = id;
    this.name = name;
    this.message = message;
    this.content = content;
  }
}
