import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

const PROTECTED_PREFIXES = ['/boards', '/schedule', '/device'];
const LOGIN_PATH = '/login';
const HOME_PATH = '/';

function isLoggedIn(request: NextRequest): boolean {
  
  const authCookie = request.cookies.get('id')?.value ?? '';
  return authCookie.length === 24;
}

function isProtectedPath(pathname: string): boolean {
  return PROTECTED_PREFIXES.some((prefix) => pathname.startsWith(prefix));
}

export function middleware(request: NextRequest) {
  const { pathname, search } = request.nextUrl;
  const loggedIn = isLoggedIn(request);
  const protectedPath = isProtectedPath(pathname);
  
  if (pathname === LOGIN_PATH && loggedIn) {
    return NextResponse.redirect(new URL(HOME_PATH, request.url));
  }

  if (protectedPath && !loggedIn) {
    const loginUrl = new URL(LOGIN_PATH, request.url);
    loginUrl.searchParams.set('from', `${pathname}${search}`);
    return NextResponse.redirect(loginUrl);
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};
