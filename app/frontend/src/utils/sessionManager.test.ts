import { describe, it, expect, beforeEach, vi } from 'vitest';
import { getSessionId, clearSession } from './sessionManager';

describe('sessionManager', () => {
  beforeEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
  });

  describe('getSessionId', () => {
    it('should create a new session ID if none exists', () => {
      const sessionId = getSessionId();
      expect(sessionId).toBeDefined();
      expect(sessionId).toMatch(/^session_\d+_[a-z0-9]+$/);
    });

    it('should return the same session ID on subsequent calls', () => {
      const firstCall = getSessionId();
      const secondCall = getSessionId();
      expect(firstCall).toBe(secondCall);
    });

    it('should retrieve existing session ID from localStorage', () => {
      const existingSessionId = 'session_123_abc';
      localStorage.setItem('sessionId', existingSessionId);
      
      const sessionId = getSessionId();
      expect(sessionId).toBe(existingSessionId);
    });

    it('should store session ID in localStorage', () => {
      const sessionId = getSessionId();
      const storedId = localStorage.getItem('sessionId');
      expect(storedId).toBe(sessionId);
    });
  });

  describe('clearSession', () => {
    it('should remove session ID from localStorage', () => {
      localStorage.setItem('sessionId', 'test_session');
      clearSession();
      expect(localStorage.getItem('sessionId')).toBeNull();
    });

    it('should work even if no session exists', () => {
      expect(() => clearSession()).not.toThrow();
      expect(localStorage.getItem('sessionId')).toBeNull();
    });
  });
});

