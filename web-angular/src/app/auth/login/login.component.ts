import {Component, OnDestroy} from '@angular/core';
import {NgForm} from "@angular/forms";
import {DataStorageService} from "../../shared/data-storage.service";
import {Subscription} from "rxjs";
import {AuthorizationService} from "../service/authorization.service";
import {LoginRequest} from "../shared/login-request.type";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {
  errorMessage: string = '';
  loginErrorSubscription: Subscription;
  loading: boolean = false;

  constructor(
    private dataStorageService: DataStorageService,
    private authorizationService: AuthorizationService
  ) {
    this.loginErrorSubscription = this.authorizationService.getLoginError().subscribe(error => {
      this.errorMessage = error;
      this.loading = false;
    });
  }

  ngOnDestroy(): void {
    this.loginErrorSubscription.unsubscribe();
  }

  onSubmit(form: NgForm) {
    if (!form.valid) {
      return;
    }

    const username = form.value.username;
    const password = form.value.password;

    const loginRequest: LoginRequest = new LoginRequest(
      username, password
    );

    this.loading = true;

    this.dataStorageService.login(loginRequest).subscribe(
      () => {
        this.loading = false;
      },
      () => {
        this.loading = false;
      }
    );

    form.reset();
  }
}
