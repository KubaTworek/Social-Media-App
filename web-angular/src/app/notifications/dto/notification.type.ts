export class Notification {
  public name: string;
  public message: string;
  public content: string;

  constructor(name: string, message: string, content: string) {
    this.name = name;
    this.message = message;
    this.content = content;
  }
}
