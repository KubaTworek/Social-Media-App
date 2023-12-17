import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";
import {UserData} from "../shared/user-data-type";

@Injectable({
  providedIn: 'root',
})
export class AuthorizationService {
  private loginErrorSubject = new BehaviorSubject<string>('');
  private registerErrorSubject = new BehaviorSubject<string>('');

  constructor(
    private router: Router,
  ) {
    this.setupInteractionsListener();
  }

  getLoginError(): Observable<string> {
    return this.loginErrorSubject.asObservable();
  }

  getRegisterError(): Observable<string> {
    return this.registerErrorSubject.asObservable();
  }

  handleLogin(userData: UserData) {
    if (userData) {
      this.storeUserData(userData);
      this.router.navigate(['/home']);
    }
  }

  handleRegister() {
    this.router.navigate(['auth/login']);
  }

  logout() {
    this.removeUserData();
    this.router.navigate(['auth/login']);
  }

  handleLoginError(errorMessage: string) {
    this.loginErrorSubject.next(errorMessage);
  }

  handleRegisterError(errorMessage: string) {
    this.registerErrorSubject.next(errorMessage);
  }

  getUserData(): UserData {
    const userDataJson = sessionStorage.getItem("userData");
    return userDataJson ? JSON.parse(userDataJson) : null;
  }

  private setupInteractionsListener() {
    document.addEventListener("mousemove", () => {
      if (this.isSessionExpired()) {
        this.logout();
      }
    });
  }

  private storeUserData(userData: UserData) {
    sessionStorage.setItem('userData', JSON.stringify(userData));
  }

  private removeUserData() {
    sessionStorage.removeItem('userData');
  }

  private isSessionExpired() {
    const userDataJson = sessionStorage.getItem("userData");
    const logoutTime = userDataJson ? Number(JSON.parse(userDataJson).tokenExpirationDate) : null;

    return logoutTime !== null && logoutTime <= new Date().getTime();
  }
}
